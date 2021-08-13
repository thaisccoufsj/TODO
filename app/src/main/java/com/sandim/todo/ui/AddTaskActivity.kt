package com.sandim.todo.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sandim.todo.R
import com.sandim.todo.databinding.ActivityAddTaskBinding
import com.sandim.todo.datasource.TaskDataSource
import com.sandim.todo.extensions.format
import com.sandim.todo.extensions.text
import com.sandim.todo.model.Task
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        insertListeners()
    }

    private fun insertListeners(){
        binding.tilDate.editText?.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timezone = TimeZone.getDefault()
                val offset = timezone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager,getString(R.string.tag_datepicker))
        }

        binding.tilTime.editText?.setOnClickListener{
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else "${timePicker.minute}"
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else "${timePicker.hour}"
                binding.tilTime.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager,getString(R.string.tag_timepicker))
        }

        binding.btnCreateTask.setOnClickListener {
            TaskDataSource.insertTask(Task(
                title = binding.tilTitle.text,
                description =  binding.tilDescription.text,
                date = binding.tilDate.text,
                time = binding.tilTime.text,
                1
            ))
        }

        binding.btnCancel.setOnClickListener {
            TODO("confirmação")
            finish()
        }

    }

}