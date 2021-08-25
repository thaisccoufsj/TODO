package com.sandim.todo.datasource

import com.sandim.todo.model.Todo

object TaskDataSource {
    private val list = arrayListOf<Todo>()

    fun getList() = list.toList()

    fun insertTask(todo: Todo) {
        if (todo.id == 0){
            list.add(todo.copy(id = list.size + 1))
        }else{
            list.remove(todo)
            list.add(todo)
        }
    }

    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun deleteTask(todo: Todo) {
        list.remove(todo)
    }


}