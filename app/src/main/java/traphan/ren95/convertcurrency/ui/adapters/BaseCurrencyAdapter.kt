package traphan.ren95.convertcurrency.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import traphan.ren95.convertcurrency.ui.recyclerdragandrop.entity.BaseCurrencyHeader
import traphan.ren95.convertcurrency.ui.recyclerdragandrop.entity.BaseCurrencyViewEntity
import traphan.ren95.convertcurrency.ui.recyclerdragandrop.entity.RECYCLER_CARD_ITEM_TYPE
import traphan.ren95.convertcurrency.ui.recyclerdragandrop.helpers.ItemTouchHelperAdapter
import traphan.ren95.convertcurrency.ui.recyclerdragandrop.helpers.OnStartDragListener
import kotlinx.android.synthetic.main.base_currency_choose_item.view.*
import kotlinx.android.synthetic.main.header_base_currency.view.*
import traphan.ren95.convertcurrency.R
import java.util.*

class BaseCurrencyAdapter(dragListener: OnStartDragListener, context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemTouchHelperAdapter {

    private val context = context
    private var indexOtherCurrency = 4
    private val dragListener: OnStartDragListener = dragListener
    private var currencyEntities: MutableList<BaseCurrencyViewEntity>? = null
    private val firstHeaderItem: BaseCurrencyHeader =
        BaseCurrencyHeader(
            context.getString(R.string.base_currency_txt),
            0
        )
    private val secondHeaderItem: BaseCurrencyHeader =
        BaseCurrencyHeader(
            context.getString(R.string.nominal_currency_txt),
            2
        )
    private lateinit var thirdHeaderItem: BaseCurrencyHeader


    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition == 1 && (toPosition == firstHeaderItem.position || secondHeaderItem.position == toPosition || thirdHeaderItem.position == toPosition)) {
            return false
        }
        if (fromPosition == 3 && currencyEntities?.get(4) is BaseCurrencyHeader) {
            return false
        }
        if (toPosition > secondHeaderItem.position && fromPosition != 1) {
            val targetCurrency = currencyEntities?.get(fromPosition)
            currencyEntities?.removeAt(fromPosition)
            currencyEntities?.add(toPosition, targetCurrency!!)
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
        this.currencyEntities = currencyEntities
//        notifyDataSetChanged()
    }

    fun setIndex(index: Int) {
        this.indexOtherCurrency = index
        thirdHeaderItem = BaseCurrencyHeader(
            context.getString(R.string.non_currency_txt),
            indexOtherCurrency
        )
        currencyEntities!!.add(firstHeaderItem.position, firstHeaderItem)
        currencyEntities!!.add(secondHeaderItem.position, secondHeaderItem)
        currencyEntities!!.add(thirdHeaderItem.position, thirdHeaderItem)
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

    fun getAllUserCurrency(): List<BaseCurrencyViewEntity> {
        var userCurrency: MutableList<BaseCurrencyViewEntity> = mutableListOf()
        return if (currencyEntities.isNullOrEmpty()) {
            userCurrency
        } else {
            userCurrency.add(0, currencyEntities?.get(1)!!)
            var startOtherCurrency = 0
            var endOtherCurrency = 0
            var counter = 0
            currencyEntities?.forEach {
                if (it is BaseCurrencyHeader) {
                    if(it.name == secondHeaderItem.name) {
                        startOtherCurrency = counter
                    } else {
                        endOtherCurrency = counter
                    }
                }
                counter++
            }
            userCurrency.addAll(currencyEntities!!.subList(startOtherCurrency + 1, endOtherCurrency))
            userCurrency
        }
    }

    inner class BaseCurrencyItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(position: Int) {
            val currency = currencyEntities?.get(position)
            if (currency is traphan.ren95.convertcurrency.ui.recyclerdragandrop.entity.BaseCurrencyItem) {
                if (currency.patch != null && currency.patch.isNotEmpty()) {
                    Picasso.get().load("file://" + currency.patch).placeholder(R.drawable.ic_simple_flag).error(R.drawable.ic_simple_flag).into(itemView.currency_icon)
                } else {
                    itemView.currency_icon.setImageResource(R.drawable.ic_simple_flag)
                }
                itemView.currency_information.text = currency.name
                itemView.reorder_item.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    false
                }
            }
        }
    }

    inner class HeaderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(position: Int) {
            val currency = currencyEntities?.get(position)
            if (currency is BaseCurrencyHeader) {
                itemView.header_text.text = currency.name
            }
        }
    }
}