package com.sandim.todo.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sandim.todo.model.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoRepoImpl(val application: Application):TodoRepo {

    private var todoDao :TodoDao

    init{
        val database = AppDatabase.getInstance(application)
        todoDao = database.todoDao()
    }

    override fun getTodo(id: Long, callback: RepositoryCallback<Todo>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val todo = todoDao.getTodo(id)
                withContext(Dispatchers.Main){
                    callback.onSucesso(todo)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    callback.onFalha(e)
                }
            }
        }
    }

    override fun getAll(callback: RepositoryCallback<List<Todo>>){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val list = todoDao.getAll()
                withContext(Dispatchers.Main){
                    callback.onSucesso(list)
                }
            }catch(e:Exception){
                withContext(Dispatchers.Main){
                    callback.onFalha(e)
                }
            }
        }

    }

    override fun insert(todo: Todo,callback: RepositoryCallback<Long>){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = todoDao.insert(todo)
                withContext(Dispatchers.Main){
                    callback.onSucesso(id)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    callback.onFalha(e)
                }
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
            withContext(Dispatchers.Main){
                if(rows > 0) callback.onSucesso(rows)
                else callback.onFalha(Exception("Falha ao atualizar"))
            }
        }
    }

    override fun delete(todo: Todo,callback: RepositoryCallback<Int>){
        CoroutineScope(Dispatchers.IO).launch {
            val rows:Int = try {
                todoDao.delete(todo)
            }catch (e:Exception){
                0
            }
            withContext(Dispatchers.Main){
                if(rows > 0) callback.onSucesso(rows)
                else callback.onFalha(Exception("Falha ao deletar"))
            }
        }
    }

}