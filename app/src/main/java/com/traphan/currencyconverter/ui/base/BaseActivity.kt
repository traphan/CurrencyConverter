package com.traphan.currencyconverter.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.traphan.currencyconverter.ui.dialog.ExitDialog
import com.traphan.currencyconverter.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {
    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    override fun onBackPressed() {
        ExitDialog().show(this)
    }
}
