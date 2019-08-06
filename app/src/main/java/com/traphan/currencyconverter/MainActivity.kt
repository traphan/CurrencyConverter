package com.traphan.currencyconverter

import android.os.Bundle
import com.traphan.currencyconverter.base.BaseActivity
import dagger.android.AndroidInjection

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

    }
}
