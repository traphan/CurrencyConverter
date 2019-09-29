package traphan.ren95.convertcurrency.ui.dialog

import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import traphan.ren95.convertcurrency.R

class NetworkDialog {
    fun show(activity: AppCompatActivity) {
        val alertDialog = build(activity)
        alertDialog.show()
    }

    private fun build(activity: AppCompatActivity): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(activity.getString(R.string.network_msg_title))
        alertDialogBuilder
            .setMessage(activity.getString(R.string.network_msg))
            .setCancelable(false)
            .setPositiveButton(activity.getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                val dialogIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                dialogIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(dialogIntent)
            })
            .setNegativeButton(activity.getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
                activity.finish()
            })
        return alertDialogBuilder.create()
    }


}