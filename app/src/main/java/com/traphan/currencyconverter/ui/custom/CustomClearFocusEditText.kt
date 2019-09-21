package com.traphan.currencyconverter.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import android.widget.Toast
import kotlinx.android.synthetic.main.currency_card_item.view.*

class CustomClearFocusEditText: AppCompatEditText {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet , defStyleAttr: Int): super(context,  attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("1", "clearFocus")
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(windowToken, 0)
            clearFocus()
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }
}