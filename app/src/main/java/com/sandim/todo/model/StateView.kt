package com.sandim.todo.model

sealed class StateView<out T> {
    object Loading : StateView<Nothing>()
    data class Message(val msg:String):StateView<Nothing>()
    data class DataLoaded<T>(val data:T):StateView<T>()
    data class DataSaved<T>(val data:T):StateView<T>()
    object DataDeleted : StateView<Nothing>()
    data class Error(val error:Throwable) : StateView<Nothing>()
}