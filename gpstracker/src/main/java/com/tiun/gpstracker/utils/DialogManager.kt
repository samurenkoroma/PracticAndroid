package com.tiun.gpstracker.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.tiun.gpstracker.R

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

    interface Command {
        fun run()
    }
}