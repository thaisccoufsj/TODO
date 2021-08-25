package com.sandim.todo.features.listTodo;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandim.todo.databinding.AdapterTodoBinding
import com.sandim.todo.model.Todo

class TodoListAdapter(
    private val onClickEdit: (Todo, Int) -> Unit,
    private val onClickDelete:(Todo,Int) ->Unit
    ): RecyclerView.Adapter<TodoListAdapter.ViewHolder>()  {

    private var todoList = listOf<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val binding: AdapterTodoBinding,
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
            }
        }
    }

    fun updateList(listOfTodos: List<Todo>){
        todoList = listOfTodos
        notifyDataSetChanged()
    }

}
