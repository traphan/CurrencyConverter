package traphan.ren95.convertcurrency.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import traphan.ren95.convertcurrency.ui.KeyboardView
import traphan.ren95.convertcurrency.ui.adapters.CurrenciesAdapter
import traphan.ren95.convertcurrency.ui.base.BaseFragment
import traphan.ren95.convertcurrency.ui.uimodel.CurrencyViewEntity
import traphan.ren95.convertcurrency.ui.viewmodel.CurrencyViewModel
import com.traphan.recycler.PagerSnapHelper
import com.traphan.recycler.RecyclerItemClickListener
import com.traphan.recycler.RecyclerSnapItemListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.currency_calculation_fragment.*
import traphan.ren95.convertcurrency.R

class CurrencyCalculationFragment : BaseFragment(), RecyclerItemClickListener.OnRecyclerViewItemClickListener, CurrenciesAdapter.CurrencyCalculation, KeyboardView.KeyboardControl {

    override fun hideKeyboard() {
        keyboard.setEditText(null)
        keyboard.setKeyboardControlListener(null)
        keyboard.visibility = GONE
        currenciesAdapter.clearTextWatcher()
    }

    override fun showKeyboard(editText: EditText) {
        keyboard.setKeyboardControlListener(this)
        keyboard.visibility = VISIBLE
        keyboard.setEditText(editText)
    }

    override fun switchCurrency(currencyViewEntity: CurrencyViewEntity) {
        currencyViewModel.switchCurrency(currencyViewEntity)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyCalculationFragment()
    }

    lateinit var currenciesAdapter: CurrenciesAdapter

    lateinit var currencyViewModel: CurrencyViewModel

    private lateinit var currencyRecalculate: LiveData<CurrencyViewEntity>

    override fun onItemClick(parentView: View, childView: View, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.currency_calculation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseView()
    }

    private fun initialiseView() {
        currencyViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrencyViewModel::class.java)
        currenciesAdapter = CurrenciesAdapter(this)
        currency_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        currency_recycler_view.adapter = currenciesAdapter
        currency_recycler_view.addOnItemTouchListener(RecyclerItemClickListener(context!!, this))
        val startSnapHelper = PagerSnapHelper(object : RecyclerSnapItemListener {
            override fun onItemSnap(position: Int) {
                val uri = currenciesAdapter.getImageCurrency(position)
                if (uri != "") {
                    Picasso.get().load(currenciesAdapter.getImageCurrency(position)).into(background)
                    background.visibility = VISIBLE
                } else {
                    background.visibility = GONE
                }
            }
        })
        startSnapHelper.attachToRecyclerView(currency_recycler_view)
        currencyViewModel.getAllViewCurrency().observe(this, Observer {currencies ->
            currenciesAdapter.setData(currencies.toMutableList())
        })
        toolbarListener(this)
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}
