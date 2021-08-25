package com.sandim.todo.data.repository

interface RepositoryCallback<T> {
    fun onSucesso(todos: T?)
    fun onFalha(t:Throwable)
}