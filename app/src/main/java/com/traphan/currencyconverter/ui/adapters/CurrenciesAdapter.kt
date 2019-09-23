package com.traphan.currencyconverter.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.CurrencyTextWatcher
import kotlinx.android.synthetic.main.currency_card_item.view.*
import com.traphan.currencyconverter.ui.util.CurrencyCalculationAdapterDiffUtil
import com.traphan.currencyconverter.ui.uimodel.CurrencyViewEntity

open class CurrenciesAdapter(activity: Activity, currencyCalculation: CurrencyCalculation): RecyclerView.Adapter<CurrenciesAdapter.CurrencyHolder>(), CurrencyTextWatcher.OnChangeCurrency {

    private var currencyViewEntities: MutableList<CurrencyViewEntity> = mutableListOf()
    private val currencyCalculation: CurrencyCalculation = currencyCalculation
    private var currencyTextWatcher: CurrencyTextWatcher? = null
    private var currentPosition: Int = 0

    interface CurrencyCalculation {
        fun switchCurrency(currencyViewEntity: CurrencyViewEntity)
        fun showKeyboard(editText: EditText)
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

    fun setData(currencyViewEntities: MutableList<CurrencyViewEntity>) {
        val currencyDiffUtilCallback = CurrencyCalculationAdapterDiffUtil(
            this.currencyViewEntities,
            currencyViewEntities
        )
        val currencyDiffResult = DiffUtil.calculateDiff(currencyDiffUtilCallback)
        this.currencyViewEntities = currencyViewEntities
        currencyDiffResult.dispatchUpdatesTo(this)
    }

    override fun onChange(currencyViewEntity: CurrencyViewEntity) {
        currencyViewEntities[currentPosition] = currencyViewEntity
    }

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var currencyViewEntity: CurrencyViewEntity
        private var position: Int? = null

        internal fun bind(position: Int) {
            currentPosition = position
            this.currencyViewEntity = currencyViewEntities[position]
            this.position = position
            itemView.name_currency.text = currencyViewEntity.name
            itemView.inputValueCurrency.setText(currencyViewEntity.currentNominal.toString())
            itemView.outputValueCurrency.setText(currencyViewEntity.total.toString())
            itemView.switch_btn.setOnClickListener { switchCurrency(currencyViewEntity) }
            if (currencyViewEntity.patchImage != null) {
                Picasso.get().load("file://" + currencyViewEntity.patchImage).placeholder(R.color.colorPrimary).error(R.color.colorPrimary).into(itemView.image)
            } else {
                itemView.image.setImageResource(R.color.colorPrimary)
            }
            itemView.inputValueCurrency.setOnClickListener {
                currencyTextWatcher?.clear()
                currencyTextWatcher = CurrencyTextWatcher(currencyViewEntities[position], true, itemView.outputValueCurrency, itemView.inputValueCurrency,
                    this@CurrenciesAdapter)
                itemView.inputValueCurrency.addTextChangedListener(currencyTextWatcher)
                showKeyboard(itemView.inputValueCurrency)
            }
            itemView.outputValueCurrency.setOnClickListener {
                currencyTextWatcher?.clear()
                currencyTextWatcher = CurrencyTextWatcher(currencyViewEntities[position], false, itemView.inputValueCurrency, itemView.outputValueCurrency,
                    this@CurrenciesAdapter)
                itemView.outputValueCurrency.addTextChangedListener(currencyTextWatcher)
                showKeyboard(itemView.outputValueCurrency)
            }
        }

        private fun showKeyboard(inputValueCurrency: EditText) {
            currencyCalculation.showKeyboard(inputValueCurrency)
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

    fun clearTextWatcher() {
        currencyTextWatcher?.clear()
    }
}
