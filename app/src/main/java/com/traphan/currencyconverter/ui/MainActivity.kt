package com.traphan.currencyconverter.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.base.BaseActivity
import com.traphan.currencyconverter.ui.viewmodel.CurrencyViewModel
import com.traphan.recycler.PagerSnapHelper
import com.traphan.recycler.RecyclerItemClickListener
import com.traphan.recycler.RecyclerSnapItemListener
import com.traphan.recycler.RecyclerViewPaginator
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), RecyclerItemClickListener.OnRecyclerViewItemClickListener, CurrenciesAdapter.CurrencyCalculation {
    override fun culculate(currencyViewEntity: CurrencyViewEntity, nominal: Float) {
        currencyViewModel.getRecalculationCurrency(currencyViewEntity, nominal)
    }


    override fun onItemClick(parentView: View, childView: View, position: Int) {

    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var currenciesAdapter: CurrenciesAdapter

    lateinit var currencyViewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
        currencyViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrencyViewModel::class.java)
        currenciesAdapter = CurrenciesAdapter(this, this)
        currency_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        currency_recycler_view.adapter = currenciesAdapter
        currency_recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, this))
        val startSnapHelper = PagerSnapHelper(object : RecyclerSnapItemListener {
                override fun onItemSnap(position: Int) {
                    background.setBackgroundResource(R.drawable.usa)
                }
            })
        startSnapHelper.attachToRecyclerView(currency_recycler_view)
        currency_recycler_view.addOnScrollListener(object : RecyclerViewPaginator(currency_recycler_view) {
            override fun isLastPage(): Boolean {
                return true
            }


            override fun loadMore(page: Long?) {

            }

            override fun loadFirstData(page: Long?) {

            }
        })
        currencyViewModel.getAllViewCurrency().observe(this, Observer {currencies -> currenciesAdapter.setData(currencies)})

    }
}
