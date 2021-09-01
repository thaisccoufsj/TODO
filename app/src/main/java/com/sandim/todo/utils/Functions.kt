package com.sandim.todo.utils

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.sandim.todo.features.dialogs.Loading

fun createLoading(activity: Activity):Loading{
    val fm = (activity as FragmentActivity).supportFragmentManager
    val loading = Loading.newInstance()
    val ft = fm.beginTransaction()
    ft.add(loading, "loading")
    ft.commitAllowingStateLoss()
    return loading
}

fun closeLoading(activity:Activity,loading:Loading?):Loading?{
    if (loading != null) {
        val fm = (activity as FragmentActivity).supportFragmentManager
        val ft = fm.beginTransaction()
        ft.remove(loading)
        ft.commitAllowingStateLoss()
    }
    return null
}