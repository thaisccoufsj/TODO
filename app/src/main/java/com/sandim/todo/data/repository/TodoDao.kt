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

    @Insert
    fun insert(todo:Todo):Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(todo:Todo):Int

    @Delete
    fun delete(todo:Todo):Int

}