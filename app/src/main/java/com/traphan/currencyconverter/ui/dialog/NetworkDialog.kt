package com.traphan.currencyconverter.ui.dialog

import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class NetworkDialog {
    fun show(activity: AppCompatActivity) {
        val alertDialog = build(activity)
        alertDialog.show()
    }

    private fun build(activity: AppCompatActivity): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("Wifi Settings")
        alertDialogBuilder
            .setMessage("Do you want to enable WIFI ?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                val dialogIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                dialogIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(dialogIntent)
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                activity.finish()
            })
        return alertDialogBuilder.create()
    }


}