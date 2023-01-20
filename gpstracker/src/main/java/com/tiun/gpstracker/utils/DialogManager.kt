package com.tiun.gpstracker.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import com.tiun.gpstracker.R
import com.tiun.gpstracker.databinding.SaveDialogBinding

object DialogManager {
    fun showLocationEnableDialog(context: Context, command: Command) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(R.string.location_dialog_title)
        dialog.setMessage(context.getString(R.string.location_dialog_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ -> command.run() }

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { _, _ -> dialog.dismiss() }

        dialog.show()
    }

    fun showSaveTrackDialog(context: Context, command: Command) {
        val builder = AlertDialog.Builder(context)
        val binding = SaveDialogBinding.inflate(LayoutInflater.from(context), null, false)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.apply {
            bSave.setOnClickListener {
                command.run()
                dialog.dismiss()
            }
            bCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    interface Command {
        fun run()
    }
}