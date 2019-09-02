package com.traphan.currencyconverter.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.recyclerhelper.ItemTouchHelperAdapter
import com.traphan.currencyconverter.ui.recyclerhelper.OnStartDragListener
import kotlinx.android.synthetic.main.base_currency_choose_item.view.*
import kotlinx.android.synthetic.main.header_base_currency.view.*
import java.util.*

class BaseCurrencyAdapter(dragListener:OnStartDragListener, context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    private val dragListener:OnStartDragListener = dragListener
    private var currencyEntities: MutableList<BaseCurrencyViewEntity>? = null
    private val firstHeaderItem: BaseCurrencyHeader = BaseCurrencyHeader(context.getString(R.string.base_currency_txt), 0)
    private val secondHeaderItem: BaseCurrencyHeader = BaseCurrencyHeader(context.getString(R.string.nominal_currency_txt), 2)
    private val thirdHeaderItem: BaseCurrencyHeader = BaseCurrencyHeader(context.getString(R.string.non_currency_txt), 4)

    private val context = context
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition > thirdHeaderItem.position && toPosition >= secondHeaderItem.position && toPosition > firstHeaderItem.position) {
            thirdHeaderItem.position = thirdHeaderItem.position + 1
            currencyEntities?.set(thirdHeaderItem.position - 1, thirdHeaderItem)
            Collections.swap(currencyEntities, toPosition, thirdHeaderItem.position)
            notifyItemMoved(fromPosition, toPosition)
        } else {
            Collections.swap(currencyEntities, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
        return true
    }

    override fun onItemDismiss(position: Int) {
        currencyEntities?.minus(position)
        notifyItemRemoved(position)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == RECYCLER_CARD_ITEM_TYPE.ITEM.ordinal) {
            val view = layoutInflater.inflate(R.layout.base_currency_choose_item, parent, false)
            BaseCurrencyItem(view)
        } else {
            val view = layoutInflater.inflate(R.layout.header_base_currency, parent, false)
            HeaderItem(view)
        }
    }

    override fun getItemCount(): Int {
        return if (currencyEntities != null && currencyEntities?.isNotEmpty()!!) {
            currencyEntities!!.size
        } else {
            0
        }
    }

    fun setData(currencyEntities: MutableList<BaseCurrencyViewEntity>) {
        currencyEntities.add(firstHeaderItem.position, firstHeaderItem)
        currencyEntities.add(secondHeaderItem.position, secondHeaderItem)
        currencyEntities.add(thirdHeaderItem.position, thirdHeaderItem)
        this.currencyEntities = currencyEntities
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseCurrencyItem) {
            if (currencyEntities != null && currencyEntities!!.isNotEmpty()) {
                holder.bind(position)
            }
        } else {
            if (holder is HeaderItem) {
                holder.bind(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val baseCurrencyViewEntity: BaseCurrencyViewEntity = currencyEntities?.get(position)!!
        return baseCurrencyViewEntity.type.ordinal
    }

    inner class BaseCurrencyItem(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {

        }

        internal fun bind(position: Int) {
            val currency = currencyEntities?.get(position)
            if (currency is com.traphan.currencyconverter.ui.BaseCurrencyItem) {
                itemView.currency_icon.setImageResource(R.drawable.ic_russia)
                itemView.currency_information.text = currency.name
                itemView.base_currency_background.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    false
                }
            }
        }
    }

    inner class HeaderItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {

        }

        internal fun bind(position: Int) {
            itemView.header_text.text = firstHeaderItem.name
        }
    }
}


