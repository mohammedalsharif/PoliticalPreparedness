package com.example.android.politicalpreparedness.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R

fun Activity.openApSetting(){
    val intent = Intent()
    intent.data = Uri.fromParts("package", this.packageName, null)
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}
fun Fragment.showAlterDialog(title: String, message:String, okAction:()->Unit){
    AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setCancelable(false)
        .setMessage(message)
        .setPositiveButton(getString(R.string.ok)) { _, _ ->
            okAction()
        }.create().show()
}