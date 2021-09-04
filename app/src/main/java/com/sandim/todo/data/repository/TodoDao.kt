package com.sandim.todo.data.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sandim.todo.model.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM TODO")
    fun getAll(): List<Todo>

    @Query("SELECT * FROM TODO WHERE id=:id")
    fun getTodo(id:Long) : Todo

    @Query("SELECT * FROM TODO WHERE done = 1")
    fun listTodoDone():List<Todo>

    @Query("SELECT * FROM TODO WHERE done = 0")
    fun getTodoPending():List<Todo>

    @Query("SELECT * FROM TODO WHERE date = :date")
    fun getTodoToday(date:String):List<Todo>

    @Query("SELECT * FROM TODO WHERE substr(date,4) = substr(:date,4)")
    fun getTodoMonth(date:String):List<Todo>

    @Insert
    fun insert(todo:Todo):Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(todo:Todo):Int

    @Delete
    fun delete(todo:Todo):Int

}