package com.sandim.todo.features

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sandim.todo.data.repository.TodoRepo
import com.sandim.todo.features.detailTodo.DetailTodoActivity
import com.sandim.todo.features.detailTodo.DetailTodoViewModel
import com.sandim.todo.features.listTodo.ListTodosActivity
import com.sandim.todo.features.listTodo.viewModel.ListTodoViewModel

class ViewModelFactory(private val repository: TodoRepo, private val bundle: Bundle?):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //return ListTodoViewModel(repository) as T

        if(modelClass.isAssignableFrom(ListTodoViewModel::class.java)){
            return ListTodoViewModel(repository) as T
        }else if(modelClass.isAssignableFrom(DetailTodoViewModel::class.java)){
            return DetailTodoViewModel(repository,bundle) as T
        }

        throw IllegalArgumentException("Unable to construct ViewModel")

    }

}