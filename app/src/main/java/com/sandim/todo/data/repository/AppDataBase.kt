package com.sandim.todo.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sandim.todo.model.Todo

@Database(entities = arrayOf(Todo::class), version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object{
        @Volatile private var instance:AppDatabase? = null

        fun getInstance(context: Context):AppDatabase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it}
            }

        fun buildDatabase(context: Context) =
               Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java,"lista_tarefas")
                   .fallbackToDestructiveMigration()
                   .build()


    }

}