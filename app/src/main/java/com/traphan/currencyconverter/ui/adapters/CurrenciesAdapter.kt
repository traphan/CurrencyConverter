package com.traphan.currencyconverter.ui.adapters

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.currency_card_item.view.*
import java.lang.ClassCastException
import com.traphan.currencyconverter.ui.CurrencyCalculationAdapterDiffUtil
import com.traphan.currencyconverter.ui.CurrencyViewEntity
import kotlin.annotation.Target as Target1
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.traphan.currencyconverter.R


open class CurrenciesAdapter(activity: Activity, currencyCalculation: CurrencyCalculation): RecyclerView.Adapter<CurrenciesAdapter.CurrencyHolder>() {

    private val activity = activity
    private var currencyViewEntities: MutableList<CurrencyViewEntity> = mutableListOf()
    private val currencyCalculation: CurrencyCalculation = currencyCalculation
    private var currentPosition: Int = 0
    var positionItemFocus = MutableLiveData<Int>()
    var isInputNow = false

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
        if (currencyViewEntities != null && currencyViewEntities.isNotEmpty()) {
            holder.bind(position)
        }
    }

    override fun onViewDetachedFromWindow(holder: CurrencyHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemView.inputValueCurrency.isFocused) {
            positionItemFocus.postValue(holder.adapterPosition)
            Log.d("1","onViewDetachedFromWindow " + positionItemFocus.value.toString())
        }
    }

    override fun onViewAttachedToWindow(holder: CurrencyHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.adapterPosition == positionItemFocus.value) {
            holder.itemView.inputValueCurrency.requestFocus()
        }
        Log.d("1", "onViewAttachedToWindow " + positionItemFocus.value.toString())
    }

    fun setData(currencyViewEntities: MutableList<CurrencyViewEntity>) {
        val currencyDiffUtilCallback = CurrencyCalculationAdapterDiffUtil(this.currencyViewEntities, currencyViewEntities)
        val currencyDiffResult = DiffUtil.calculateDiff(currencyDiffUtilCallback)
        this.currencyViewEntities = currencyViewEntities
        currencyDiffResult.dispatchUpdatesTo(this)
    }

    fun setData(currencyViewEntity: CurrencyViewEntity) {
        currencyViewEntities[currentPosition] = currencyViewEntity
        notifyItemChanged(currentPosition)
    }

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var currencyViewEntity: CurrencyViewEntity
        private var position: Int? = null

        init {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            itemView.layoutParams = RecyclerView.LayoutParams((width * 0.85f).toInt(), RecyclerView.LayoutParams.WRAP_CONTENT)

            itemView.outputValueCurrency.addTextChangedListener(object : TextWatcher {
                private var isInflate = true

                override fun onTextChanged(s: CharSequence, start: Int, b: Int, c: Int) {
                    if (!isInflate) {
                        currencyViewEntity.cursorPositionTotal =
                            if (currencyViewEntity.cursorPositionTotal.toString().length > s.length) {
                                start
                            } else {
                                start + 1
                            }
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, c: Int, a: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    synchronized(currencyViewEntity) {
                        try {
                            if (isInflate) {
                                itemView.outputValueCurrency.setSelection(currencyViewEntity.cursorPositionTotal)
                                isInflate = false
                            }
                            if (!currencyViewEntity.total.equals(s.toString().toFloat())) {
                                currentPosition = position!!
                                currencyCalculation.calculate(currencyViewEntity, itemView.inputValueCurrency.text.toString().toFloat(), s.toString().toFloat())
                                isInflate = true
                            }
                        } catch (e: ClassCastException) {

                        }
                    }
                }
            })
            itemView.inputValueCurrency.addTextChangedListener(object : TextWatcher {
                private var isInflate = true
                override fun onTextChanged(s: CharSequence, start: Int, b: Int, c: Int) {
                    if (!isInflate) {
                        currencyViewEntity.cursorPositionNominal =
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
                    synchronized(currencyViewEntity) {
                        try {
                            if (isInflate) {
                                itemView.inputValueCurrency.setSelection(currencyViewEntity.cursorPositionNominal)
                                isInflate = false
                            }
                            if (!currencyViewEntity.currentNominal.equals(s.toString().toFloat())) {
                                currentPosition = position!!
                                currencyCalculation.calculate(currencyViewEntity, s.toString().toFloat(), itemView.outputValueCurrency.text.toString().toFloat())
                                isInflate = true
                            }
                        } catch (e: ClassCastException) {

                        }
                    }
                }
            })
        }

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
                if (!b && position == positionItemFocus.value) {
                    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.hideSoftInputFromWindow(itemView.inputValueCurrency.windowToken, 0)
                    itemView.inputValueCurrency.clearFocus()
                    isInputNow = false
//                    android.os.Handler().postDelayed({positionItemFocus.value = null}, 300)
                } else {
                    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.showSoftInput(itemView.inputValueCurrency, 0)
                    isInputNow = true
                    android.os.Handler().postDelayed({positionItemFocus.value = null}, 300)

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
