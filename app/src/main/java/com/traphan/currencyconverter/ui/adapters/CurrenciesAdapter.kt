package com.traphan.currencyconverter.ui.adapters

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.traphan.currencyconverter.R
import kotlinx.android.synthetic.main.currency_card_item.view.*
import com.traphan.currencyconverter.ui.util.CurrencyCalculationAdapterDiffUtil
import com.traphan.currencyconverter.ui.uimodel.CurrencyViewEntity

//TODO in first holder don't work exit dialog after press back button when focused input edit text
open class CurrenciesAdapter(activity: Activity, currencyCalculation: CurrencyCalculation): RecyclerView.Adapter<CurrenciesAdapter.CurrencyHolder>() {

    private var currencyViewEntities: MutableList<CurrencyViewEntity> = mutableListOf()
    private val currencyCalculation: CurrencyCalculation = currencyCalculation
    private var currentPosition: Int = 0
    var positionItemFocus = MutableLiveData<Int>()
    var idOnFocusInputText: Int? = null
    var isOnFocusInputText: Boolean? = null
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

    interface CurrencyCalculation {
        fun calculate(currencyViewEntity: CurrencyViewEntity, nominal: Float, total: Float)
        fun switchCurrency(currencyViewEntity: CurrencyViewEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.swiped_root_card_item, parent, false)
        return CurrencyHolder(view)
    }

    override fun getItemCount(): Int {
        return currencyViewEntities.size
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        if (currencyViewEntities.isNotEmpty()) {
            holder.bind(position)
        }
    }

    override fun onViewDetachedFromWindow(holder: CurrencyHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemView.inputValueCurrency.isFocused) {
            positionItemFocus.postValue(holder.adapterPosition)
            isOnFocusInputText = true
            Log.d("1","onViewDetachedFromWindow inputValueCurrency" + positionItemFocus.value.toString())
        } else {
            if (holder.itemView.outputValueCurrency.isFocused) {
                positionItemFocus.postValue(holder.adapterPosition)
                isOnFocusInputText = false
                Log.d("1","onViewDetachedFromWindow outputValueCurrency" + positionItemFocus.value.toString())
            }
        }
    }

    override fun onViewAttachedToWindow(holder: CurrencyHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.adapterPosition == positionItemFocus.value && idOnFocusInputText == null) {
            idOnFocusInputText = if (isOnFocusInputText != null && isOnFocusInputText == true) {
                holder.itemView.inputValueCurrency.requestFocus()
                holder.itemView.inputValueCurrency.id
            } else {
                holder.itemView.outputValueCurrency.requestFocus()
                holder.itemView.outputValueCurrency.id
            }
            Handler().postDelayed({positionItemFocus.postValue(null)}, 0)
            Log.d("1", "requestFocus onViewAttachedToWindow")
        } else {
            if (holder.adapterPosition == positionItemFocus.value) {
                idOnFocusInputText = null
                isOnFocusInputText = null
                Handler().postDelayed({positionItemFocus.postValue(null)}, 0)
                Log.d("1", "idOnFocusInputText null")
            }
        }
    }

    fun setData(currencyViewEntities: MutableList<CurrencyViewEntity>) {
        val currencyDiffUtilCallback = CurrencyCalculationAdapterDiffUtil(
            this.currencyViewEntities,
            currencyViewEntities
        )
        val currencyDiffResult = DiffUtil.calculateDiff(currencyDiffUtilCallback)
        this.currencyViewEntities = currencyViewEntities
        currencyDiffResult.dispatchUpdatesTo(this)
    }

    fun setData(currencyViewEntity: CurrencyViewEntity) {
        currencyViewEntities[currentPosition] = currencyViewEntity
        val currencyDiffUtilCallback = CurrencyCalculationAdapterDiffUtil(
            this.currencyViewEntities,
            currencyViewEntities
        )
        val currencyDiffResult = DiffUtil.calculateDiff(currencyDiffUtilCallback)
        this.currencyViewEntities = currencyViewEntities
        currencyDiffResult.dispatchUpdatesTo(this)
    }

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var currencyViewEntity: CurrencyViewEntity
        private var position: Int? = null

        internal fun bind(position: Int) {
            this.currencyViewEntity = currencyViewEntities[position]
            this.position = position
            itemView.name_currency.text = currencyViewEntity.name
            synchronized(currencyViewEntity) {
                itemView.inputValueCurrency.setText(currencyViewEntity.currentNominal.toString())
                itemView.outputValueCurrency.setText(currencyViewEntity.total.toString())
            }
            itemView.switch_btn.setOnClickListener { switchCurrency(currencyViewEntity) }
            if (currencyViewEntity.patchImage != null) {
                Picasso.get().load("file://" + currencyViewEntity.patchImage).placeholder(R.color.colorPrimary).error(R.color.colorPrimary).into(itemView.image)
            } else {
                itemView.image.setImageResource(R.color.colorPrimary)
            }
            itemView.inputValueCurrency.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                if (b && position == positionItemFocus.value) {
                    imm!!.showSoftInput(itemView.inputValueCurrency, 0)
                } else {
                    if (positionItemFocus.value == null && position == 0 && !b) {
                        positionItemFocus.postValue(null)
                        Log.d("1", "itemView.inputValueCurrency.clearFocus() pos 0" +  itemView.inputValueCurrency.isFocused)
                    }
                }
            }
            itemView.outputValueCurrency.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                if (b && position == positionItemFocus.value) {
                    imm!!.showSoftInput(itemView.outputValueCurrency, 0)
                } else {
                    if (positionItemFocus.value == null && position == 0 && !b) {
                        positionItemFocus.postValue(null)
                        Log.d("1", "itemView.inputValueCurrency.clearFocus() pos 0" +  itemView.outputValueCurrency.isFocused)
                    }
                }
            }
        }

        private fun switchCurrency(currencyViewEntity: CurrencyViewEntity) {
            currencyCalculation.switchCurrency(currencyViewEntity)
        }
    }

    fun getImageCurrency(position: Int): String {
        return if (currencyViewEntities != null && currencyViewEntities.size > position) {
            "file://" + currencyViewEntities.get(position).patchImage
        } else ""
    }
}
