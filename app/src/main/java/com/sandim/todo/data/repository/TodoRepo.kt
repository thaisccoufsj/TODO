package com.sandim.todo.data.repository

import com.sandim.todo.model.Todo

interface TodoRepo {
    fun getAll(callback: RepositoryCallback<List<Todo>>)
    fun insert(todo: Todo,callback: RepositoryCallback<Long>)
    fun update(todo: Todo,callback: RepositoryCallback<Int>)
    fun delete(todo: Todo,callback: RepositoryCallback<Int>)
    fun getTodo(id: Long,callback: RepositoryCallback<Todo>)
}