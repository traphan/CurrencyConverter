package com.traphan.currencyconverter.ui.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.ui.base.BaseViewModel
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.repository.CurrencyRepository
import com.traphan.currencyconverter.repository.CurrencyRepositoryImpl
import com.traphan.currencyconverter.ui.CurrencyViewEntity
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

class CurrencyViewModel @Inject constructor(currencyApi: CurrencyApi, currencyDao: CurrencyDao, imageDao: ImageDao, userCurrencyDao: UserCurrencyDao,
                                            application: Application): BaseViewModel() {

    private val context: Context = application.baseContext
    private var currencyRepository: CurrencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi, imageDao, userCurrencyDao, application)
    private var currencyViewEntitiesLiveData: MutableLiveData<Set<CurrencyViewEntity>> = MutableLiveData()
    private var currencyViewEntityLiveData: MutableLiveData<CurrencyViewEntity> = MutableLiveData()
    private var baseCurrencyViewEntityLiveData: MutableLiveData<List<BaseCurrencyViewEntity>> = MutableLiveData()
    private var userCurrencyLiveData: MutableLiveData<List<UserCurrency>> = MutableLiveData()
    private var userCurrencyCountLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var userCurrencySaveLiveData: MutableLiveData<Boolean>
    private lateinit var baseCurrency: CurrencyJoinImage
    private lateinit var idBaseCurrency: String
    private var userCurrencyCurrent: List<UserCurrency>? = null

    fun getAllViewCurrency(): LiveData<Set<CurrencyViewEntity>> {
        loadAllUserCurrency().observeForever{
            if(it != null && it.isNotEmpty()) {
                userCurrencyCurrent = it
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

    fun getRecalculationCurrency(currencyViewEntity: CurrencyViewEntity, nominal: Float, total: Float): LiveData<CurrencyViewEntity> {
        currencyViewEntityLiveData.value = getCalculationCurrency(currencyViewEntity, nominal, total)
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

    fun switchCurrency(currencyViewEntity: CurrencyViewEntity) {
        var switchUserCurrency: List<UserCurrency> = mutableListOf()
        if (userCurrencyCurrent != null && userCurrencyCurrent!!.isNotEmpty()) {
            userCurrencyCurrent!!.forEach {
                switchUserCurrency = if (it.idCurrency == currencyViewEntity.idRemote) {
                    val userCurrency = UserCurrency(null, it.idCurrency, true)
                    switchUserCurrency.plusElement(userCurrency)
                } else {
                    val userCurrency = UserCurrency(null, it.idCurrency, false)
                    switchUserCurrency.plusElement(userCurrency)
                }
            }
        }
        if (switchUserCurrency.isNotEmpty()) {
            addDisposable(currencyRepository.insertAllUserCurrency(switchUserCurrency).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({Toast.makeText(context, "true", Toast.LENGTH_LONG).show()}, {Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()})
            )
        }
    }

    fun isInternetAvailable(): Boolean {
        return currencyRepository.isInternetAvailable()
    }
}