package com.traphan.currencyconverter.ui

import androidx.annotation.IdRes
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.EditText
import android.widget.FrameLayout
import com.traphan.currencyconverter.R

class KeyboardView : FrameLayout, View.OnClickListener {

    interface KeyboardControl {
        fun hideKeyboard()
    }

    private var keyboardControl: KeyboardControl? = null
    private var mPasswordField: EditText? = null

    val inputText: String
        get() = mPasswordField!!.text.toString()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.keyboard, this)
        initViews()
    }

    fun setEditText(editText: EditText?) {
        mPasswordField = editText
    }

    fun setKeyboardControlListener(keyboardControl: KeyboardControl?) {
        this.keyboardControl = keyboardControl
    }

    private fun initViews() {
        `$`<View>(R.id.t9_key_0).setOnClickListener(this)
        `$`<View>(R.id.t9_key_1).setOnClickListener(this)
        `$`<View>(R.id.t9_key_2).setOnClickListener(this)
        `$`<View>(R.id.t9_key_3).setOnClickListener(this)
        `$`<View>(R.id.t9_key_4).setOnClickListener(this)
        `$`<View>(R.id.t9_key_5).setOnClickListener(this)
        `$`<View>(R.id.t9_key_6).setOnClickListener(this)
        `$`<View>(R.id.t9_key_7).setOnClickListener(this)
        `$`<View>(R.id.t9_key_8).setOnClickListener(this)
        `$`<View>(R.id.t9_key_9).setOnClickListener(this)
        `$`<View>(R.id.t9_key_clear).setOnClickListener(this)
        `$`<View>(R.id.t9_key_backspace).setOnClickListener(this)
        `$`<View>(R.id.t9_key_point).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        // handle number button click
        if (v.tag != null && "number_button" == v.tag) {
            mPasswordField!!.append((v as TextView).text)
            return
        }
        when (v.id) {
            R.id.t9_key_clear -> { // handle clear button
                keyboardControl!!.hideKeyboard()
            }
            R.id.t9_key_backspace -> { // handle backspace button
                // delete one character
                val editable = mPasswordField!!.text
                val charCount = editable.length
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount)
                }
            }
        }
    }


    internal fun <T : View> `$`(@IdRes id: Int): T {
        return super.findViewById<View>(id) as T
    }
}