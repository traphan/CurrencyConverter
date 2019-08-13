package com.traphan.currencyconverter.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.ui.base.BaseViewModel
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.repository.CurrencyRepository
import com.traphan.currencyconverter.repository.CurrencyRepositoryImpl
import com.traphan.currencyconverter.ui.CurrencyViewEntity
import com.traphan.currencyconverter.СurrencyСalculation.getCalculateAllCurrency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(currencyApi: CurrencyApi, currencyDao: CurrencyDao ): BaseViewModel() {

    private var currencyRepository: CurrencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi)

    fun getAllViewCurrency(): LiveData<List<CurrencyViewEntity>> {
        var currencyViewEntitiesLiveData: MutableLiveData<List<CurrencyViewEntity>> = MutableLiveData()
        addDisposable(currencyRepository.getAllCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe{currencies -> currencyViewEntitiesLiveData.value = getCalculateAllCurrency(currencies)})
        return currencyViewEntitiesLiveData
    }
}