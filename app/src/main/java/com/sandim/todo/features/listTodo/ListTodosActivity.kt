package com.sandim.todo.features.listTodo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandim.todo.R
import com.sandim.todo.data.repository.TodoRepo
import com.sandim.todo.data.repository.TodoRepoImpl
import com.sandim.todo.databinding.ActivityListTodosBinding
import com.sandim.todo.features.detailTodo.DetailTodoActivity
import com.sandim.todo.model.StateView
import com.sandim.todo.features.listTodo.viewModel.ListTodoViewModel
import com.sandim.todo.features.listTodo.viewModel.ListTodoViewModelFactory
import com.sandim.todo.utils.Constants
import com.sandim.todo.utils.Constants.Companion.KEY_EXTRA_TODO_ACTION
import com.sandim.todo.utils.Constants.Companion.KEY_EXTRA_TODO_ADD

class ListTodosActivity:AppCompatActivity() {

    //data source -room
    private lateinit var binding: ActivityListTodosBinding
    private lateinit var todoListAdapter:TodoListAdapter

    private val repository:TodoRepo by lazy{
        TodoRepoImpl()
    }

    private val viewModel: ListTodoViewModel by lazy{
        ViewModelProvider(this,ListTodoViewModelFactory(repository)).get(ListTodoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTodosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.app_name)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, DetailTodoActivity::class.java)
            intent.putExtra(KEY_EXTRA_TODO_ACTION, KEY_EXTRA_TODO_ADD)
            createActivityLauncher.launch(intent)
        }

        todoListAdapter = TodoListAdapter(onClickEdit = {todo,position ->
            detailTodo(todo.id,position)
        },onClickDelete = {todo,position ->
            viewModel.deleteItem(todo)
        })

        binding.rvTasks.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(this@ListTodosActivity)
        }

        viewModel.stateView.observe(this,{stateView ->

            when(stateView){
                is StateView.Loading -> {
                    binding.viewLoading.root.visibility = View.VISIBLE
                }

                is StateView.DataLoaded -> {
                    binding.viewLoading.root.visibility = View.GONE
                    todoListAdapter.updateList(stateView.data)
                }

                is StateView.Error -> {
                    binding.viewLoading.root.visibility = View.GONE
                    Toast.makeText(this,stateView.error.message,Toast.LENGTH_SHORT).show()
                }


            }

        })

        viewModel.getAllTodo()

    }

    private fun detailTodo(todoId:Int,position:Int){
        val intent = Intent(this, DetailTodoActivity::class.java)
        intent.putExtra(Constants.KEY_EXTRA_TODO_ID,todoId)
        intent.putExtra(Constants.KEY_EXTRA_TODO_INDEX,position)
        createActivityLauncher.launch(intent)
    }

    private val createActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        when(result.resultCode){
            Constants.CODE_RESULT_CREATE_SUCESS -> {
               viewModel.getAllTodo()
            }

            Constants.CODE_RESULT_REMOVE_SUCESS -> {
                result.data?.getIntExtra(Constants.KEY_EXTRA_TODO_INDEX,0)?.let{
                   viewModel.getAllTodo()
                }
            }

        }
    }
}