package com.sandim.todo.features.detailTodo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sandim.todo.R
import com.sandim.todo.data.repository.RepositoryCallback
import com.sandim.todo.data.repository.TodoRepo
import com.sandim.todo.data.repository.TodoRepoImpl
import com.sandim.todo.databinding.ActivityDetailTodoBinding
import com.sandim.todo.extensions.text
import com.sandim.todo.model.Todo
import com.sandim.todo.utils.Constants

class DetailTodoActivity:AppCompatActivity() {
        private lateinit var binding : ActivityDetailTodoBinding
        private lateinit var todo : Todo
        private var positionOfTodo : Int? = -1

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityDetailTodoBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val toolbar = binding.toolbar
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }

            positionOfTodo = intent.extras?.getInt(Constants.KEY_EXTRA_TODO_INDEX)
            intent.extras?.getInt(Constants.KEY_EXTRA_TODO_ID)?.let { todoId ->
                showDataFromDataSource(todoId)
            }

            binding.btnCreateTask.setOnClickListener {
                val repository: TodoRepo = TodoRepoImpl()
                repository.insert(Todo(0,
                                        binding.tilTitle.text
                                        ,binding.tilDescription.text,
                                        binding.tilDate.text,
                                        binding.tilTime.text,
                                        false),object : RepositoryCallback<Long> {
                    override fun onSucesso(todos: Long?) {
                        binding.tvHeader.text = getString(R.string.label_header_edit_todo).replace("#id",todos.toString(),true)
                    }

                    override fun onFalha(t: Throwable) {
                        Toast.makeText(this@DetailTodoActivity,"Erro ao inserir no banco",Toast.LENGTH_LONG).show()
                    }

                })
            }

            binding.btnCancel.setOnClickListener {
                finish()
            }

        }

    private fun removeTodo(todo: Todo, position: Int?) {
//        Local
//        DataSourceLocal.removeTodo(todo)

//        Remoto
        /*DataSourceRemote().remove(todo.id, object : RepositoryCallback<Todo> {
            override fun onSucesso(todos: Todo?) {
                val data = Intent()
                data.putExtra(Constants.KEY_EXTRA_TODO_INDEX, position)
                setResult(Constants.CODE_RESULT_REMOVE_SUCCESS, data)
                finish()
            }

            override fun onFalha(t: Throwable) {
                Toast.makeText(this@DetailTodoActivity, "Falha na remoção", Toast.LENGTH_SHORT).show()
            }
        })*/
    }

    private fun showDataFromDataSource(idTodo: Int) {
//        Local
//        todo = DataSourceLocal.getTodoById(idTodo)

//        Remoto
       /* DataSourceRemote().getById(idTodo, object : RepositoryCallback<Todo> {
            override fun onSucesso(todos: Todo?) {
                todos?.let {
                    todo = todos

                    binding.apply {
                        tvHeader.text =
                            String.format(getString(R.string.detail_todo_tv_header_text, todo.title, todo.id))
                        tvTitleTodoValue.text = todo.title
                        tvDescriptionTodoValue.text = todo.description
                    }
                }
            }

            override fun onFalha(t: Throwable) {
                Toast.makeText(this@DetailTodoActivity, "Falha na busca", Toast.LENGTH_SHORT).show()
            }
        })*/
    }

}