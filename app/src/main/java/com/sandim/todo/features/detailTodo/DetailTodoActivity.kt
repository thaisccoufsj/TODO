package com.sandim.todo.features.detailTodo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

            binding.btnDelete.visibility = View.GONE

            intent.extras?.getInt(Constants.KEY_EXTRA_TODO_ID,0)?.let { todoId ->
               if(todoId != 0){
                   binding.toolbar.title = getString(R.string.appBar_detail_activity_edit)
                   binding.btnDelete.visibility = View.VISIBLE
               }
            }



            viewModel.stateView.observe(this,{stateView ->
                when(stateView){
                    is StateView.Loading -> {
                       loading = createLoading(this)
                    }

                    is StateView.DataDeleted -> {
                        MaterialAlertDialogBuilder(this)
                            .setMessage(getString(R.string.text_todo_deleted))
                            .setNeutralButton(getString(R.string.dialog_ok)) { _, _ ->
                                finish()
                            }
                            .show()
                    }

                    is StateView.DataLoaded -> {
                        loading = closeLoading(this,loading)
                        val todo = stateView.data
                        binding.tilId.text = "${todo.id}"
                        binding.tilTitle.text = todo.title
                        binding.tilDescription.text = todo.description
                        binding.tilDate.text = todo.date
                        binding.tilTime.text = todo.time
                        binding.swDone.isActivated = todo.done
                    }

                    is StateView.DataSaved -> {
                        loading = closeLoading(this,loading)
                        val todo = stateView.data
                        binding.tilId.text = "${todo.id}"
                        binding.toolbar.title = getString(R.string.appBar_detail_activity_edit)
                        binding.btnDelete.visibility = View.VISIBLE
                        Snackbar.make(binding.root,getString(R.string.text_todo_saved), Snackbar.LENGTH_LONG).show()
                    }

                    is StateView.Error -> {
                        loading = closeLoading(this,loading)
                        Snackbar.make(binding.root,stateView.error.message ?: "Erro desconhecido", Snackbar.LENGTH_LONG).show()
                    }

                }
            })

            binding.btnSave.setOnClickListener {

                if(validarCampos()){
                    viewModel.saveTodo(Todo(if(binding.tilId.text.isEmpty()) 0 else binding.tilId.text.toInt() ,
                                            binding.tilTitle.text,
                                            binding.tilDescription.text,
                                            binding.tilDate.text,
                                            binding.tilTime.text,
                                            binding.swDone.isChecked))
                }else{
                    Snackbar.make(binding.root,getString(R.string.text_verify_fields),Snackbar.LENGTH_LONG).show()
                }

            }

            binding.btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(this@DetailTodoActivity)
                    .setTitle(getString(R.string.dialog_title_delete_todo))
                    .setMessage(getString(R.string.dialog_message_delete_todo))
                    .setPositiveButton(getString(R.string.dialog_confirm_positive)) { _, _ ->
                        viewModel.deleteTodo(Todo(if(binding.tilId.text.isEmpty()) 0 else binding.tilId.text.toInt() ,
                            binding.tilTitle.text,
                            binding.tilDescription.text,
                            binding.tilDate.text,
                            binding.tilTime.text,
                            binding.swDone.isChecked))
                    }
                    .setNegativeButton(getString(R.string.dialog_confirm_negative)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
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