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
import com.traphan.currencyconverter.CurrencyCalculation.getCalculateCurrency
import com.traphan.currencyconverter.CurrencyCalculation.getCalculationAllCurrency
import com.traphan.currencyconverter.CurrencyCalculation.getCalculationCurrency
import com.traphan.currencyconverter.database.dao.UserCurrencyDao
import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.database.entity.UserCurrency
import com.traphan.currencyconverter.ui.recyclerdragandrop.entity.BaseCurrencyItem
import com.traphan.currencyconverter.ui.recyclerdragandrop.entity.BaseCurrencyViewEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(currencyApi: CurrencyApi, currencyDao: CurrencyDao, imageDao: ImageDao, userCurrencyDao: UserCurrencyDao): BaseViewModel() {

    private var currencyRepository: CurrencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi, imageDao, userCurrencyDao)
    private var currencyViewEntitiesLiveData: MutableLiveData<Set<CurrencyViewEntity>> = MutableLiveData()
    private var currencyViewEntityLiveData: MutableLiveData<CurrencyViewEntity> = MutableLiveData()
    private var baseCurrencyViewEntityLiveData: MutableLiveData<List<BaseCurrencyViewEntity>> = MutableLiveData()
    private var userCurrencyLiveData: MutableLiveData<List<UserCurrency>> = MutableLiveData()
    private var userCurrencyCountLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var userCurrencySaveLiveData: MutableLiveData<Boolean>
    private lateinit var baseCurrency: CurrencyJoinImage
    private lateinit var idBaseCurrency: String

    fun getAllViewCurrency(): LiveData<Set<CurrencyViewEntity>> {
        loadAllUserCurrency().observeForever{
            if(it != null && it.isNotEmpty()) {
                var ids: List<String> = listOf()
                it.forEach {
                    ids = ids.plusElement(it.idCurrency)
                    if (it.isBase) {
                        idBaseCurrency = it.idCurrency
                    }
                }
                addDisposable(currencyRepository.loadAllCurrencyJoin(ids).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe{currencies ->
                        currencies.forEach {
                            if (it.currencyEntity.idRemote == idBaseCurrency) {
                                baseCurrency = it
                            }
                        }
                        currencyViewEntitiesLiveData.value = getCalculationAllCurrency(currencies.filter { it.currencyEntity.idRemote != idBaseCurrency }, baseCurrency)

                    })}}
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
                            BaseCurrencyItem(it.currencyEntity.charCode + " " + it.currencyEntity.name, "", it.currencyEntity.idRemote)
                        )
                    }
                    baseCurrencyViewEntityLiveData.value = baseCurrencyViewEntity
                }
            }
        })
        return baseCurrencyViewEntityLiveData
    }

    fun insertAllUserCurrency(userCurrencies: List<BaseCurrencyViewEntity>): LiveData<Boolean> {
        userCurrencySaveLiveData = MutableLiveData()
        addDisposable(currencyRepository.insertAllUserCurrency(getAllUserCurrency(userCurrencies)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({userCurrencySaveLiveData.value = true}, {userCurrencySaveLiveData.value = false})
        )
        return userCurrencySaveLiveData
    }

    fun getCountUserCurrency(): LiveData<Boolean>  {
        addDisposable(currencyRepository.getCountUserCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userCurrencyCountLiveData.value = it != null && it[0]!! > 1}, {userCurrencyCountLiveData.value = false}))
        return userCurrencyCountLiveData
    }

    private fun loadAllUserCurrency(): LiveData<List<UserCurrency>> {
        addDisposable(currencyRepository.loadAllUserCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{userCurrencies ->
            run{
                userCurrencyLiveData.value = userCurrencies
            }})
        return userCurrencyLiveData
    }

    private fun getAllUserCurrency(userCurrencies: List<BaseCurrencyViewEntity>): List<UserCurrency> {
        var userCurrencyEntities: List<UserCurrency> = listOf()
        userCurrencies.forEach {
            if (it is BaseCurrencyItem) {
                userCurrencyEntities = userCurrencyEntities.plusElement(UserCurrency(null, it.idCurrency, it == userCurrencies[0]))
            }
        }
        return userCurrencyEntities
    }

    override fun onCleared() {
        onStop()
        super.onCleared()
    }
}