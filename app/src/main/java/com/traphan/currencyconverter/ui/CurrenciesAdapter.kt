package com.traphan.currencyconverter.ui

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traphan.currencyconverter.R
import kotlinx.android.synthetic.main.currency_card_item.view.*
import java.lang.ClassCastException


open class CurrenciesAdapter(activity: Activity, currencyCalculation: CurrencyCalculation): RecyclerView.Adapter<CurrenciesAdapter.CurrencyHolder>() {

    private val activity = activity
    private var currencyViewEntities: Set<CurrencyViewEntity>? = null
    private val currencyCalculation: CurrencyCalculation = currencyCalculation

    interface CurrencyCalculation {
        fun culculate(currencyViewEntity: CurrencyViewEntity, nominal: Float)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.swiped_root_card_item, parent, false)
        return CurrencyHolder(view)
    }

    override fun getItemCount(): Int {
        return if (currencyViewEntities != null && currencyViewEntities?.isNotEmpty()!!) {
            currencyViewEntities!!.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        if (currencyViewEntities != null && currencyViewEntities?.isNotEmpty()!!) {
            holder.bind(currencyViewEntities!!.elementAt(position))
        }
    }

    fun setData(currencyViewEntities: Set<CurrencyViewEntity>) {
        this.currencyViewEntities = currencyViewEntities
        notifyDataSetChanged()
    }

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            itemView.layoutParams = RecyclerView.LayoutParams((width * 0.85f).toInt(), RecyclerView.LayoutParams.WRAP_CONTENT)
        }

        internal fun bind(currencyViewEntity: CurrencyViewEntity) {
            itemView.name_currency.text = currencyViewEntity.name
            itemView.inputValueCurrency.setText(currencyViewEntity.currentNominal.toString())
            itemView.outputValueCurrency.setText(currencyViewEntity.total.toString())
            itemView.background_layout.setBackgroundResource(R.drawable.usa)
            itemView.inputValueCurrency.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, st: Int, b: Int, c: Int) {
                }

                override fun beforeTextChanged(s: CharSequence, st: Int, c: Int, a: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    if (s != null) {
                        try {
                            currencyCalculation.culculate(currencyViewEntity, s.toString().toFloat())
                        }
                        catch (e: ClassCastException) {

                        }
                    }
                }
            })
        }
    }
}