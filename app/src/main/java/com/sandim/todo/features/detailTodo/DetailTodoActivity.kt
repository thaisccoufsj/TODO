package com.sandim.todo.features.detailTodo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sandim.todo.R
import com.sandim.todo.data.repository.TodoRepo
import com.sandim.todo.data.repository.TodoRepoImpl
import com.sandim.todo.databinding.ActivityDetailTodoBinding
import com.sandim.todo.extensions.format
import com.sandim.todo.extensions.text
import com.sandim.todo.features.ViewModelFactory
import com.sandim.todo.features.dialogs.Loading
import com.sandim.todo.model.StateView
import com.sandim.todo.model.Todo
import com.sandim.todo.utils.Constants
import com.sandim.todo.utils.closeLoading
import com.sandim.todo.utils.createLoading
import java.util.*

class DetailTodoActivity:AppCompatActivity() {

        private lateinit var binding : ActivityDetailTodoBinding
        private var loading:Loading? = null

        private val repository:TodoRepo by lazy{
            TodoRepoImpl(this.application)
        }

        private val viewModel : DetailTodoViewModel by lazy{
            ViewModelProvider(this,ViewModelFactory(repository,intent.extras)).get(DetailTodoViewModel::class.java)
        }

        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityDetailTodoBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.toolbar.setNavigationOnClickListener {
                finish()
            }

            intent.extras?.getInt(Constants.KEY_EXTRA_TODO_ID,0)?.let { todoId ->
               if(todoId != 0){
                   binding.toolbar.title = getString(R.string.appBar_detail_activity_edit)
               }
            }

            viewModel.stateView.observe(this,{stateView ->
                when(stateView){
                    is StateView.Loading -> {
                       loading = createLoading(this)
                    }

                    is StateView.DataLoaded -> {
                        loading = closeLoading(this,loading)
                        val todo = stateView.data
                        binding.tvHeader.text = (binding.tvHeader.text as String).replace("#id","#${todo.id}")
                        binding.tilTitle.text = todo.title
                        binding.tilDescription.text = todo.description
                        binding.tilDate.text = todo.date
                        binding.tilTime.text = todo.time
                        binding.swDone.isActivated = todo.done
                    }

                    is StateView.DataSaved -> {
                        loading = closeLoading(this,loading)
                        val todo = stateView.data
                        binding.tvHeader.text = (binding.tvHeader.text as String).replace("#id","#${todo.id}")
                        binding.toolbar.title = getString(R.string.appBar_detail_activity_edit)
                        Snackbar.make(binding.root,"Tarefa salva com sucesso", Snackbar.LENGTH_LONG).show()
                    }

                    is StateView.Error -> {
                        loading = closeLoading(this,loading)
                        Snackbar.make(binding.root,stateView.error.message ?: "Erro desconhecido", Snackbar.LENGTH_LONG).show()
                    }

                }
            })

            binding.btnSave.setOnClickListener {

                if(validarCampos()){
                    val id = Regex("[^0-9]").replace(binding.tvHeader.text.split("#")[1],"")
                    viewModel.saveTodo(Todo(if(id.isEmpty()) 0 else id.toInt() ,
                                            binding.tilTitle.text,
                                            binding.tilDescription.text,
                                            binding.tilDate.text,
                                            binding.tilTime.text,
                                            binding.swDone.isChecked))
                }else{
                    Snackbar.make(binding.root,"Por favor preencha todos campos para continuar",Snackbar.LENGTH_LONG).show()
                }

            }

            binding.btnCancel.setOnClickListener {
                setResult(RESULT_OK)
                finish()
            }

            binding.tilDate.editText?.setOnClickListener{
                val datePicker = MaterialDatePicker.Builder.datePicker().build()
                datePicker.addOnPositiveButtonClickListener {
                    val timezone = TimeZone.getDefault()
                    val offset = timezone.getOffset(Date().time) * -1
                    binding.tilDate.text = Date(it + offset).format()
                }
                datePicker.show(supportFragmentManager,getString(R.string.tag_datepicker))
            }

            binding.tilTime.editText?.setOnClickListener{
                val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .build()

                timePicker.addOnPositiveButtonClickListener {
                    val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else "${timePicker.minute}"
                    val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else "${timePicker.hour}"
                    binding.tilTime.text = "$hour:$minute"
                }

                timePicker.show(supportFragmentManager,getString(R.string.tag_timepicker))
            }

        }

    private fun validarCampos() = (binding.tilTitle.text.isNotEmpty()) && (binding.tilDescription.text.isNotEmpty()) &&
                                  (binding.tilDate.text.isNotEmpty()) && (binding.tilTime.text.isNotEmpty())

}