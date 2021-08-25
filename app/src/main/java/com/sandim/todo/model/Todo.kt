package com.sandim.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TODO")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title:String,
    val description:String,
    val date : String,
    val time :String,
    val done : Boolean
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Todo

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
