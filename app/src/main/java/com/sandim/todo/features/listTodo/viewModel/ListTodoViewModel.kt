package com.sandim.todo.features.listTodo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.sandim.todo.data.repository.RepositoryCallback
import com.sandim.todo.data.repository.TodoRepo
import com.sandim.todo.model.StateView
import com.sandim.todo.model.Todo

class ListTodoViewModel(private val repository: TodoRepo):ViewModel() {

    private val _stateView = MutableLiveData<StateView<List<Todo>>>()
    val stateView: LiveData<StateView<List<Todo>>>
        get() = _stateView

    fun getAllTodo(){
        //if(_stateView.value != null) return
        _stateView.value = StateView.Loading

        repository.getAll(object : RepositoryCallback<List<Todo>>{
            override fun onSucesso(todos: List<Todo>?) {
                _stateView.value = StateView.DataLoaded(todos ?: arrayListOf())
            }

            override fun onFalha(t: Throwable) {
               _stateView.value = StateView.Error(Exception(t.message))
            }
        })

    }

    fun deleteItem(todo:Todo){
        _stateView.value = StateView.Loading

        repository.delete(todo,object : RepositoryCallback<Int>{
            override fun onSucesso(todos: Int?) {
                if((todos ?: 0) > 0){
                    _stateView.value = StateView.Message("A tarefa foi excuída com sucesso")
                   getAllTodo()
                }else{
                    _stateView.value = StateView.Error(Exception("Não foi posssível excluir a tarefa. "))
                }
            }

            override fun onFalha(t: Throwable) {
                _stateView.value = StateView.Error(Exception("Não foi posssível excluir a tarefa. "))
            }
        })

    }

}