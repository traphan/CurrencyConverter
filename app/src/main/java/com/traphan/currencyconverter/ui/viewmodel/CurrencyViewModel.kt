package com.traphan.currencyconverter.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.ui.base.BaseViewModel
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.repository.CurrencyRepository
import com.traphan.currencyconverter.repository.CurrencyRepositoryImpl
import com.traphan.currencyconverter.ui.CurrencyViewEntity
import com.traphan.currencyconverter.CurrencyCalculation.getCalculateAllCurrency
import com.traphan.currencyconverter.CurrencyCalculation.getCalculationCurrency
import com.traphan.currencyconverter.ui.recyclerdragandrop.entity.BaseCurrencyItem
import com.traphan.currencyconverter.ui.recyclerdragandrop.entity.BaseCurrencyViewEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(currencyApi: CurrencyApi, currencyDao: CurrencyDao, imageDao: ImageDao): BaseViewModel() {

    private var currencyRepository: CurrencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi, imageDao)
    private var currencyViewEntitiesLiveData: MutableLiveData<Set<CurrencyViewEntity>> = MutableLiveData()
    private var currencyViewEntityLiveData: MutableLiveData<CurrencyViewEntity> = MutableLiveData()
    private var baseCurrencyViewEntityLiveData: MutableLiveData<List<BaseCurrencyViewEntity>> = MutableLiveData()


    fun getAllViewCurrency(): LiveData<Set<CurrencyViewEntity>> {
        addDisposable(currencyRepository.loadAllCurrencyJoin().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe{currencies -> currencyViewEntitiesLiveData.value = getCalculateAllCurrency(currencies)})
        return currencyViewEntitiesLiveData
    }

    fun getRecalculationCurrency(currencyViewEntity: CurrencyViewEntity, nominal: Float): LiveData<CurrencyViewEntity> {
        addDisposable(getCalculationCurrency(currencyViewEntity, nominal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{currencyViewEntity ->
            run {
                currencyViewEntityLiveData.value = currencyViewEntity
            }
        })
        return currencyViewEntityLiveData
    }

    fun getBaseCurrency(): LiveData<List<BaseCurrencyViewEntity>> {
        addDisposable(currencyRepository.loadAllCurrencyJoin().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            run {
                if (it != null && it.isNotEmpty()) {
                    var baseCurrencyViewEntity: List<BaseCurrencyViewEntity> = listOf()
                    it.forEach {
                        baseCurrencyViewEntity = baseCurrencyViewEntity.plusElement(
                            BaseCurrencyItem(
                                it.currencyEntity.charCode + " "
                                        + it.currencyEntity.name, ""
                            )
                        )
                    }
                    baseCurrencyViewEntityLiveData.value = baseCurrencyViewEntity
                }
            }
        })
        return baseCurrencyViewEntityLiveData
    }
}