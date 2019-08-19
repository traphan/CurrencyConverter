package com.traphan.currencyconverter.ui

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traphan.currencyconverter.R
import kotlinx.android.synthetic.main.currency_card_item.view.*

open class CurrenciesAdapter(activity: Activity): RecyclerView.Adapter<CurrenciesAdapter.CurrencyHolder>() {

    private val activity = activity
    private var currencyViewEntities: List<CurrencyViewEntity>? = null

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
            holder.bind(currencyViewEntities!![position])
        }
    }

    fun setData(currencyViewEntities: List<CurrencyViewEntity>) {
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
            itemView.currency_name.text = currencyViewEntity.name
            itemView.currency_count.setText(currencyViewEntity.currentNominal.toString())
            itemView.currency_total.text = currencyViewEntity.total.toString()
            itemView.rate.text = currencyViewEntity.rate.toString()
        }
    }
}