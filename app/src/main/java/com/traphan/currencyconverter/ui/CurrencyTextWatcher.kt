package com.traphan.currencyconverter.ui

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.traphan.currencyconverter.CurrencyCalculation.getCalculationCurrency
import com.traphan.currencyconverter.ui.uimodel.CurrencyViewEntity

class CurrencyTextWatcher(currencyViewEntity: CurrencyViewEntity, isInput: Boolean, writingEditText: EditText, currentEditText: EditText, onChangeCurrency: OnChangeCurrency) : TextWatcher {

    private val currencyViewEntity: CurrencyViewEntity = currencyViewEntity
    private val currentEditText: EditText = currentEditText
    private var isInput: Boolean = isInput
    private val onChangeCurrency: OnChangeCurrency = onChangeCurrency
    private val writingEditText: EditText = writingEditText

    init {
        Log.d("1", "CurrencyTextWatcher")
    }

    interface OnChangeCurrency {
        fun onChange(currencyViewEntity: CurrencyViewEntity)
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0 != null && p0.isNotEmpty() && p0.toString().toFloatOrNull() != null) {
            if (isInput) {
                val currencyViewEntity = getCalculationCurrency(currencyViewEntity, p0.toString().toFloat(), currencyViewEntity.total)
                writingEditText.setText(currencyViewEntity.total.toString())
                onChangeCurrency.onChange(currencyViewEntity)
            } else {
                val currencyViewEntity = getCalculationCurrency(currencyViewEntity, currencyViewEntity.currentNominal, p0.toString().toFloat())
                writingEditText.setText(currencyViewEntity.currentNominal.toString())
                onChangeCurrency.onChange(currencyViewEntity)
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    fun isInput(): Boolean {
        return isInput!!
    }

    fun getCurrentEditText(): EditText {
        return writingEditText!!
    }

    fun clear() {
        currentEditText.removeTextChangedListener(this)
        Log.d("1", "clear TextChangedListener")
    }
}