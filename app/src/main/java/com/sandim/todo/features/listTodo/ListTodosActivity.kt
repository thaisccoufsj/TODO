package com.sandim.todo.features.listTodo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sandim.todo.R
import com.sandim.todo.data.repository.TodoRepo
import com.sandim.todo.data.repository.TodoRepoImpl
import com.sandim.todo.databinding.ActivityListTodosBinding
import com.sandim.todo.features.detailTodo.DetailTodoActivity
import com.sandim.todo.model.StateView
import com.sandim.todo.features.listTodo.viewModel.ListTodoViewModel
import com.sandim.todo.features.ViewModelFactory
import com.sandim.todo.model.Todo
import com.sandim.todo.utils.Constants
import com.sandim.todo.utils.Constants.Companion.KEY_EXTRA_TODO_ACTION
import com.sandim.todo.utils.Constants.Companion.KEY_EXTRA_TODO_ADD

class ListTodosActivity:AppCompatActivity() {

    //data source -room
    private lateinit var binding: ActivityListTodosBinding
    private lateinit var todoListAdapter:TodoListAdapter

    private val repository:TodoRepo by lazy{
        TodoRepoImpl(this.application)
    }

    private val viewModel: ListTodoViewModel by lazy{
        ViewModelProvider(this, ViewModelFactory(repository,intent.extras)).get(ListTodoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTodosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = getString(R.string.app_name)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, DetailTodoActivity::class.java)
            createActivityLauncher.launch(intent)
        }

        todoListAdapter = TodoListAdapter(this,
            onClickEdit = {todo,_ ->
            detailTodo(todo.id)
        },onClickDelete = {todo,_ ->
            viewModel.deleteItem(todo)
        })

        binding.rvTasks.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(this@ListTodosActivity)
        }

        viewModel.stateView.observe(this,{stateView ->

            when(stateView){
                is StateView.Loading -> {
                    binding.rvTasks.visibility = View.GONE
                    binding.viewLoading.root.visibility = View.VISIBLE
                    binding.LLEmptyTodo.visibility = View.GONE
                }

                is StateView.DataLoaded -> {
                    binding.viewLoading.root.visibility = View.GONE
                    if(stateView.data.isEmpty()){
                        binding.rvTasks.visibility = View.GONE
                        binding.LLEmptyTodo.visibility = View.VISIBLE
                    }else{
                        binding.rvTasks.visibility = View.VISIBLE
                        binding.LLEmptyTodo.visibility = View.GONE
                    }
                    todoListAdapter.updateList(stateView.data)
                }

                is StateView.Error -> {
                    binding.viewLoading.root.visibility = View.GONE
                    binding.LLEmptyTodo.visibility = View.VISIBLE
                    binding.rvTasks.visibility = View.GONE
                    todoListAdapter.updateList(arrayListOf())
                    Snackbar.make(binding.root,stateView.error.message ?: "Erro desconhecido",Snackbar.LENGTH_LONG).show()
                }


            }

        })

        viewModel.getAllTodo()

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.menuFilterAll -> {
                    viewModel.getAllTodo()
                    true
                }

                R.id.menuFilterToday -> {
                    viewModel.getTodosToday()
                    true
                }

                R.id.menuFilterMonth -> {
                    viewModel.getTodosMonth()
                    true
                }

                R.id.menuFilterDone -> {
                    viewModel.getTodosDone()
                    true
                }

                R.id.menuFilterPending -> {
                    viewModel.getTodosPending()
                    true
                }

                else -> false

            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTodo()
    }

    private fun detailTodo(todoId:Int){
        val intent = Intent(this, DetailTodoActivity::class.java)
        intent.putExtra(Constants.KEY_EXTRA_TODO_ID,todoId)
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