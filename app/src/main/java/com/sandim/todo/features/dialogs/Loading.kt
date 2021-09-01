package com.sandim.todo.features.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sandim.todo.databinding.LoadingBinding

class Loading:DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = LoadingBinding.inflate(LayoutInflater.from(context))
        binding.progressBar.isIndeterminate = true
        isCancelable = false
        return AlertDialog.Builder(requireActivity())
                .setView(binding.root)
                .create()
    }

    companion object {
        fun newInstance(): Loading {
            return Loading()
        }
    }


}