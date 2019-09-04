package com.traphan.currencyconverter.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.BaseCurrencyFragment
import com.traphan.currencyconverter.ui.CurrencyCalculationFragment
import com.traphan.currencyconverter.viewmodelfactory.ViewModelFactory
import kotlinx.android.synthetic.main.toolbar.*
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

    internal fun toolbarListener(fragment: Fragment) {
        if (fragment is BaseCurrencyFragment) {
            calculate_btn.setOnClickListener { nextAction(CurrencyCalculationFragment.newInstance()) }
            calculate_btn.setImageResource(R.drawable.all_currency_btn_pressed)
        } else {
            all_currency.setOnClickListener { nextAction(BaseCurrencyFragment.newInstance()) }
            calculate_btn.setImageResource(R.drawable.all_currency_btn_pressed)
        }
    }

    private fun nextAction(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.base_background, fragment).addToBackStack(null).commit()
    }

    override fun onDetach() {
        super.onDetach()
    }
}
