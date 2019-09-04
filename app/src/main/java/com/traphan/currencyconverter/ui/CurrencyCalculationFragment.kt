package com.traphan.currencyconverter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.adapters.CurrenciesAdapter
import com.traphan.currencyconverter.ui.base.BaseFragment
import com.traphan.currencyconverter.ui.viewmodel.CurrencyViewModel
import com.traphan.recycler.PagerSnapHelper
import com.traphan.recycler.RecyclerItemClickListener
import com.traphan.recycler.RecyclerSnapItemListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.currency_calculation_fragment.*

class CurrencyCalculationFragment : BaseFragment(), RecyclerItemClickListener.OnRecyclerViewItemClickListener, CurrenciesAdapter.CurrencyCalculation {
    override fun switchCurrency(currencyViewEntity: CurrencyViewEntity) {
        currencyViewModel.switchCurrency(currencyViewEntity)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyCalculationFragment()
    }

    private lateinit var currenciesAdapter: CurrenciesAdapter

    lateinit var currencyViewModel: CurrencyViewModel

    override fun onItemClick(parentView: View, childView: View, position: Int) {

    }

    override fun calculate(currencyViewEntity: CurrencyViewEntity, nominal: Float, total: Float) {
        currencyViewModel.getRecalculationCurrency(currencyViewEntity, nominal, total).observe(this, Observer { currenciesAdapter.setData(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.currency_calculation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseView()
    }

    private fun initialiseView() {
        currencyViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrencyViewModel::class.java)
        currenciesAdapter = CurrenciesAdapter(getActivity()!!, this)
        currency_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        currency_recycler_view.adapter = currenciesAdapter
        currency_recycler_view.addOnItemTouchListener(RecyclerItemClickListener(context!!, this))
        val startSnapHelper = PagerSnapHelper(object : RecyclerSnapItemListener {
            override fun onItemSnap(position: Int) {
                background.setBackgroundResource(R.drawable.usa)
            }
        })
        startSnapHelper.attachToRecyclerView(currency_recycler_view)
        currencyViewModel.getAllViewCurrency().observe(this, Observer {currencies ->
            currenciesAdapter.setData(currencies.toMutableList())
        })
        toolbarListener(this)
    }
}
