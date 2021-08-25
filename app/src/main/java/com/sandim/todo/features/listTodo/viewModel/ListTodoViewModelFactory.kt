package com.sandim.todo.features.listTodo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sandim.todo.data.repository.TodoRepo

class ListTodoViewModelFactory(private val repository: TodoRepo):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListTodoViewModel(repository) as T
    }

}