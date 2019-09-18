package com.traphan.currencyconverter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.base.BaseFragment
import com.traphan.currencyconverter.ui.adapters.BaseCurrencyAdapter
import com.traphan.currencyconverter.ui.recyclerdragandrop.helpers.OnStartDragListener
import com.traphan.currencyconverter.ui.recyclerdragandrop.helpers.SimpleItemTouchHelperCallback
import com.traphan.currencyconverter.ui.viewmodel.CurrencyViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_base_currency.*
import kotlinx.android.synthetic.main.toolbar.*

class BaseCurrencyFragment : BaseFragment(), OnStartDragListener {

    lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var baseCurrencyAdapter: BaseCurrencyAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_currency, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = BaseCurrencyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseView()
    }

    private fun initialiseView() {
        baseCurrencyAdapter = BaseCurrencyAdapter(this, context!!)
        base_currency_recycler_view.setHasFixedSize(true)
        base_currency_recycler_view.adapter = baseCurrencyAdapter
        base_currency_recycler_view.layoutManager = LinearLayoutManager(this.context)
        val callback = SimpleItemTouchHelperCallback(baseCurrencyAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(base_currency_recycler_view)
        currencyViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrencyViewModel::class.java)
        currencyViewModel.getBaseCurrency().observe(this, Observer {
            baseCurrencyAdapter.setData(it.toMutableList())
            currencyViewModel.getUserCurrencyOtherCurrencyIndex().observe(this, Observer {
                baseCurrencyAdapter.setIndex(it)
            })
        })
        toolbarListener(this)
    }

    override fun toolbarListener(fragment: Fragment) {
        super.toolbarListener(fragment)
        toolbar_btn.setOnClickListener { saveUserCurrency() }
    }

    private fun saveUserCurrency() {
        currencyViewModel.insertAllUserCurrency(baseCurrencyAdapter.getAllUserCurrency()).observe(this, Observer { nextAction(CurrencyCalculationFragment.newInstance())})
    }
}
