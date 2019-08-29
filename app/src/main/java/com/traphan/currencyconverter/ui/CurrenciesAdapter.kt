package com.traphan.currencyconverter.ui

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.currency_card_item.view.*
import java.lang.ClassCastException
import com.traphan.currencyconverter.R
import java.io.File
import java.lang.Exception
import java.net.URL
import kotlin.annotation.Target as Target1


open class CurrenciesAdapter(activity: Activity, currencyCalculation: CurrencyCalculation): RecyclerView.Adapter<CurrenciesAdapter.CurrencyHolder>() {

    private val activity = activity
    private var currencyViewEntities: MutableList<CurrencyViewEntity>? = null
    private val currencyCalculation: CurrencyCalculation = currencyCalculation
    private var currentPosition: Int = 0

    interface CurrencyCalculation {
        fun calculate(currencyViewEntity: CurrencyViewEntity, nominal: Float)
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
            holder.bind(position)
        }
    }

    fun setData(currencyViewEntities: MutableList<CurrencyViewEntity>) {
        this.currencyViewEntities = currencyViewEntities
        notifyDataSetChanged()
    }

    fun setData(currencyViewEntity: CurrencyViewEntity) {
        currencyViewEntities!![currentPosition] = currencyViewEntity
        notifyItemChanged(currentPosition)
    }

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var currencyViewEntity: CurrencyViewEntity
        private var position: Int? = null
        private var isInflate = true


        init {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            itemView.layoutParams =
                RecyclerView.LayoutParams((width * 0.85f).toInt(), RecyclerView.LayoutParams.WRAP_CONTENT)
            itemView.inputValueCurrency.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, start: Int, b: Int, c: Int) {
                    if (!isInflate) {
                        currencyViewEntity.cursorPosition =
                            if (currencyViewEntity.currentNominal.toString().length > s.length) {
                                start
                            } else {
                                start + 1
                            }
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, c: Int, a: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    try {
                        if (isInflate) {
                            itemView.inputValueCurrency.setSelection(currencyViewEntity.cursorPosition)
                            isInflate = false
                        }
                        if (!currencyViewEntity.currentNominal.equals(s.toString().toFloat())) {
                            currentPosition = position!!
                            currencyCalculation.calculate(currencyViewEntity, s.toString().toFloat())
                            isInflate = true
                        }
                    } catch (e: ClassCastException) {

                    }
                }
            })
        }

        internal fun bind(position: Int) {
            this.currencyViewEntity = currencyViewEntities!![position]
            this.position = position
            itemView.name_currency.text = currencyViewEntity.name
            itemView.inputValueCurrency.setText(currencyViewEntity.currentNominal.toString())
            itemView.outputValueCurrency.setText(currencyViewEntity.total.toString())
            if (currencyViewEntity.patchImage != null) {
                Picasso.get().load("file://" + currencyViewEntity.patchImage).into(itemView.image)
            }
        }
    }

}
