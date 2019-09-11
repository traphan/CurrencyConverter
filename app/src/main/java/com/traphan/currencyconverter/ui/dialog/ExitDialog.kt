package com.traphan.currencyconverter.ui.dialog

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.traphan.currencyconverter.R

class ExitDialog {

    fun show(activity: AppCompatActivity) {
        val alertDialog = build(activity)
        alertDialog.show()
    }

    private fun build(activity: AppCompatActivity): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(activity.getString(R.string.exit))
        alertDialogBuilder
            .setMessage(activity.getString(R.string.exit_msg))
            .setCancelable(false)
            .setPositiveButton(activity.getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                activity.finish()
            })
            .setNegativeButton(activity.getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
        return alertDialogBuilder.create()
    }
}