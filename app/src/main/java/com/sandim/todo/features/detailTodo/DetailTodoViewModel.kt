package com.sandim.todo.features.detailTodo

import com.sandim.todo.data.repository.TodoRepo
import com.sandim.todo.utils.Constants
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sandim.todo.data.repository.RepositoryCallback
import com.sandim.todo.model.StateView
import com.sandim.todo.model.Todo

class DetailTodoViewModel(private val repository: TodoRepo, private val bundle: Bundle?): ViewModel() {

    private val _stateView = MutableLiveData<StateView<Todo>>()
    val stateView: LiveData<StateView<Todo>>
        get() = _stateView
    init{
        bundle?.getInt(Constants.KEY_EXTRA_TODO_ID,0)?.let { todoId ->
            loadTodo(todoId)
        }
    }


    fun loadTodo(id:Int){

        _stateView.value = StateView.Loading

        repository.getTodo(id.toLong(),object: RepositoryCallback<Todo>{
            override fun onSucesso(todos: Todo?) {
                if(todos != null) _stateView.value = StateView.DataLoaded(todos)
            }

            override fun onFalha(t: Throwable) {
                _stateView.value = StateView.Error(Exception("Erro ao carregar todo."))
            }

        })
    }

    fun saveTodo(todo:Todo){

        _stateView.value = StateView.Loading

        if(todo.id > 0){
            repository.update(todo,object:RepositoryCallback<Int>{
                override fun onSucesso(todos: Int?) {
                   _stateView.value = StateView.DataSaved(todo)
                }

                override fun onFalha(t: Throwable) {
                    _stateView.value = StateView.Error(Exception("Não foi possível salvar."))
                }

            })
        }else{
            repository.insert(todo,object : RepositoryCallback<Long>{
                override fun onSucesso(todos: Long?) {
                    _stateView.value = StateView.DataSaved(Todo(todos?.toInt() ?: 0,todo.title,todo.description,todo.date,todo.time,todo.done))
                }

                override fun onFalha(t: Throwable) {
                    _stateView.value = StateView.Error(Exception("Não foi possível salvar."))
                }

            })
        }
    }

}


