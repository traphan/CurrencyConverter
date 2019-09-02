package com.traphan.currencyconverter.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.traphan.currencyconverter.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    protected lateinit var activity: AppCompatActivity
    protected lateinit var supportFragmentManager: FragmentManager

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

     override fun onAttach(context: Context) {
         super.onAttach(context)
         this.activity = context as AppCompatActivity
         this.supportFragmentManager = activity.supportFragmentManager
    }

    override fun onDetach() {
        super.onDetach()
    }
}
