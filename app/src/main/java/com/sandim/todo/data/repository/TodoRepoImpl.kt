package com.sandim.todo.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sandim.todo.model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoRepoImpl:TodoRepo {

    private lateinit var todoDao :TodoDao

    fun TodoRepo(application: Application){
        val database = AppDatabase.getInstance(application)
        todoDao = database.todoDao()
    }

    override fun getTodo(id: Long, callback: RepositoryCallback<Todo>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val todo = todoDao.getTodo(id)
                callback.onSucesso(todo)
            }catch (e:Exception){
                callback.onFalha(e)
            }
        }
    }

    override fun getAll(callback: RepositoryCallback<List<Todo>>){

        try{
            val list = todoDao.getAll()
            callback.onSucesso(list)
        }catch(e:Exception){
            callback.onFalha(e)
        }
    }

    override fun insert(todo: Todo,callback: RepositoryCallback<Long>){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = todoDao.insert(todo)
                callback.onSucesso(id)
            }catch (e:Exception){
                callback.onFalha(e)
            }
        }
    }

    override fun update(todo:Todo,callback: RepositoryCallback<Int>){
        CoroutineScope(Dispatchers.IO).launch {

            val rows:Int = try {
                todoDao.update(todo)
            }catch (e:Exception){
                0
            }
            if(rows > 0) callback.onSucesso(rows)
            else callback.onFalha(Exception("Falha ao atualizar"))
        }
    }

    override fun delete(todo: Todo,callback: RepositoryCallback<Int>){
        CoroutineScope(Dispatchers.IO).launch {
            val rows:Int = try {
                todoDao.delete(todo)
            }catch (e:Exception){
                0
            }
            if(rows > 0) callback.onSucesso(rows)
            else callback.onFalha(Exception("Falha ao deletar"))
        }
    }

}