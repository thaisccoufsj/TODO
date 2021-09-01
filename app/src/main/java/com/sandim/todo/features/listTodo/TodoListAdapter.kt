package com.sandim.todo.features.listTodo;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandim.todo.R
import com.sandim.todo.databinding.ListItemTodoBinding
import com.sandim.todo.model.Todo

class TodoListAdapter(
    private val onClickEdit: (Todo, Int) -> Unit,
    private val onClickDelete:(Todo,Int) ->Unit
    ): RecyclerView.Adapter<TodoListAdapter.ViewHolder>()  {

    private var todoList = listOf<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClickEdit,onClickDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.bind(todo, position)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    inner class ViewHolder(
        private val binding: ListItemTodoBinding,
        val onClickEdit: (Todo, Int) -> Unit,
        val onClickDelete: (Todo,Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo, position: Int){

            binding.ivEdit.setOnClickListener{
                onClickEdit(todo,position)
            }

            binding.ivDelete.setOnClickListener {
                onClickDelete(todo,position)
            }

            binding.apply {
                tvTitle.text = todo.title
                tvContent.text = todo.description
                tvDate.text = todo.date
                tvTime.text = todo.time
                ivDone.setImageResource(if(todo.done) R.drawable.ic_done else R.drawable.ic_pending)
            }
        }
    }

    fun updateList(listOfTodos: List<Todo>){
        todoList = listOfTodos
        notifyDataSetChanged()
    }

}
