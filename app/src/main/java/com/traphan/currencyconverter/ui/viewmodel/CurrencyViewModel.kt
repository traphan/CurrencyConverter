package com.traphan.currencyconverter.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.traphan.currencyconverter.ui.base.BaseViewModel
import com.traphan.currencyconverter.database.dao.CurrencyDao
import com.traphan.currencyconverter.repository.CurrencyRepository
import com.traphan.currencyconverter.repository.CurrencyRepositoryImpl
import com.traphan.currencyconverter.ui.uimodel.CurrencyViewEntity
import com.traphan.currencyconverter.CurrencyCalculation.getCalculationAllCurrency
import com.traphan.currencyconverter.CurrencyCalculation.getCalculationCurrency
import com.traphan.currencyconverter.api.CurrencyApi
import com.traphan.currencyconverter.database.dao.ImageDao
import com.traphan.currencyconverter.database.dao.UserCurrencyDao
import com.traphan.currencyconverter.database.entity.CurrencyJoinImage
import com.traphan.currencyconverter.database.entity.UserCurrency
import com.traphan.currencyconverter.ui.recyclerdragandrop.entity.BaseCurrencyItem
import com.traphan.currencyconverter.ui.recyclerdragandrop.entity.BaseCurrencyViewEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(currencyDao: CurrencyDao, currencyApi: CurrencyApi,  userCurrencyDao: UserCurrencyDao, imageDao: ImageDao,
                                            application: Application): BaseViewModel() {

    private val context: Context = application.baseContext
    private var currencyRepository: CurrencyRepository = CurrencyRepositoryImpl(currencyDao, currencyApi, userCurrencyDao, imageDao, application)
    private var currencyViewEntitiesLiveData: MutableLiveData<Set<CurrencyViewEntity>> = MutableLiveData()
    private var currencyViewEntityLiveData: MutableLiveData<CurrencyViewEntity> = MutableLiveData()
    private var baseCurrencyViewEntityLiveData: MutableLiveData<List<BaseCurrencyViewEntity>> = MutableLiveData()
    private var userCurrencyLiveData: MutableLiveData<List<UserCurrency>> = MutableLiveData()
    private var userCurrencyCountLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var userCurrencySaveLiveData: MutableLiveData<Boolean>
    private lateinit var userCurrencyOtherCurrencyIndex: MutableLiveData<Int>
    private lateinit var baseCurrency: CurrencyJoinImage
    private lateinit var idBaseCurrency: String
    private var userCurrencyCurrent: List<UserCurrency>? = null
    private var indexNotBaseCurrency: Int = 4


    fun getAllViewCurrency(): LiveData<Set<CurrencyViewEntity>> {
        loadAllUserCurrency().observeForever {
            if (it != null && it.isNotEmpty()) {
                userCurrencyCurrent = it
                var ids: List<String> = listOf()
                it.forEach {
                    ids = ids.plusElement(it.idCurrency)
                    if (it.isBase) {
                        idBaseCurrency = it.idCurrency
                    }
                }
                addDisposable(currencyRepository.loadAllCurrencyJoin(ids).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ currencies ->
                        currencies.forEach {
                            if (it.currencyEntity.idRemote == idBaseCurrency) {
                                baseCurrency = it
                            }
                        }
                        currencyViewEntitiesLiveData.postValue(getCalculationAllCurrency(
                            currencies.filter { it.currencyEntity.idRemote != idBaseCurrency }, baseCurrency
                        )
                        )
                    }, { Log.d("1", it.message)}))
            }
        }
        return currencyViewEntitiesLiveData
    }

    fun getRecalculationCurrency(currencyViewEntity: CurrencyViewEntity, nominal: Float, total: Float): MutableLiveData<CurrencyViewEntity> {
        currencyViewEntityLiveData.postValue(getCalculationCurrency(currencyViewEntity, nominal, total))
        return currencyViewEntityLiveData
    }

    private fun getBaseCurrencyUnsorted(): LiveData<List<BaseCurrencyViewEntity>> {
        addDisposable(currencyRepository.loadAllCurrencyJoin().subscribeOn(Schedulers.io()).subscribe {
            run {
                if (it != null && it.isNotEmpty()) {
                    var baseCurrencyViewEntity: List<BaseCurrencyViewEntity> = listOf()
                    it.forEach {
                        val iconPatch = if (it.imageEntity != null && it.imageEntity.isNotEmpty() && it.imageEntity[0].icon != null) {
                            it.imageEntity[0].icon
                        } else ""
                        baseCurrencyViewEntity = baseCurrencyViewEntity.plusElement(
                            BaseCurrencyItem(
                                it.currencyEntity.charCode + " " + it.currencyEntity.name,
                                iconPatch!!,
                                it.currencyEntity.idRemote
                            )
                        )
                    }
                    baseCurrencyViewEntityLiveData.postValue(baseCurrencyViewEntity)
                }
            }
        })
        return baseCurrencyViewEntityLiveData
    }

    fun getBaseCurrency(): LiveData<List<BaseCurrencyViewEntity>> {
        var swapCurrencyList: MutableList<CurrencyJoinImage> = mutableListOf()
        addDisposable(currencyRepository.loadAllCurrencyJoin().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (it != null && it.isNotEmpty()) {
                swapCurrencyList = it.toMutableList()
            }
        })
        isInsertUserCurrency().observeForever {
            if (it) {
                loadAllUserCurrency().observeForever { userCurrencies ->
                    if (userCurrencies != null && userCurrencies.isNotEmpty()) {
                        if (swapCurrencyList.isNotEmpty()) {
                            var localIndex = 1
                            userCurrencies.forEach { userCurrency ->
                                if (userCurrency.isBase) {
                                    val indexBaseCurrency = swapCurrencyList.indexOfFirst { it.currencyEntity.idRemote == userCurrency.idCurrency }
                                    Collections.swap(swapCurrencyList, 0, indexBaseCurrency)
                                } else {
                                    val indexCurrency = swapCurrencyList.indexOfFirst { it.currencyEntity.idRemote == userCurrency.idCurrency }
                                    Collections.swap(swapCurrencyList, localIndex, indexCurrency)
                                    localIndex++
                                    indexNotBaseCurrency++
                                }
                            }
                            if (swapCurrencyList != null && swapCurrencyList.isNotEmpty()) {
                                var baseCurrencyViewEntity: List<BaseCurrencyViewEntity> = listOf()
                                swapCurrencyList.forEach {
                                    val iconPatch = if (it.imageEntity != null && it.imageEntity.isNotEmpty() && it.imageEntity[0].icon != null) {
                                        it.imageEntity[0].icon
                                    } else ""
                                    baseCurrencyViewEntity = baseCurrencyViewEntity.plusElement(
                                        BaseCurrencyItem(
                                            it.currencyEntity.charCode + " " + it.currencyEntity.name,
                                            iconPatch!!,
                                            it.currencyEntity.idRemote
                                        )
                                    )
                                }
                                indexNotBaseCurrency--
                                baseCurrencyViewEntityLiveData.postValue(baseCurrencyViewEntity)
                            }
                        }
                    }
                }
            } else {
                indexNotBaseCurrency = 4
                baseCurrencyViewEntityLiveData = getBaseCurrencyUnsorted() as MutableLiveData<List<BaseCurrencyViewEntity>>
            }
        }
        return baseCurrencyViewEntityLiveData
    }

    fun insertAllUserCurrency(userCurrencies: List<BaseCurrencyViewEntity>): LiveData<Boolean> {
        userCurrencySaveLiveData = MutableLiveData()
        addDisposable(
            currencyRepository.insertAllUserCurrency(getAllUserCurrency(userCurrencies)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userCurrencySaveLiveData.postValue(true)  }, { userCurrencySaveLiveData.postValue(false)  })
        )
        return userCurrencySaveLiveData
    }

    fun isInsertUserCurrency(): LiveData<Boolean> {
        addDisposable(
            currencyRepository.getCountUserCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { userCurrencyCountLiveData.postValue(it != null && it[0]!! > 1 )},
                    { userCurrencyCountLiveData.postValue(false)})
        )
        return userCurrencyCountLiveData
    }

    private fun loadAllUserCurrency(): LiveData<List<UserCurrency>> {
        addDisposable(currencyRepository.loadAllUserCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { userCurrencies ->
            run {
                userCurrencyLiveData.postValue(userCurrencies)
            }
        })
        return userCurrencyLiveData
    }

    private fun getAllUserCurrency(userCurrencies: List<BaseCurrencyViewEntity>): List<UserCurrency> {
        var userCurrencyEntities: List<UserCurrency> = listOf()
        userCurrencies.forEach {
            if (it is BaseCurrencyItem) {
                userCurrencyEntities =
                    userCurrencyEntities.plusElement(UserCurrency(null, it.idCurrency, it == userCurrencies[0]))
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
            addDisposable(currencyRepository.insertAllUserCurrency(switchUserCurrency).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({}, {}))
        }
    }

    fun isInternetAvailable(): Boolean {
        return currencyRepository.isInternetAvailable()
    }

    fun getUserCurrencyOtherCurrencyIndex(): LiveData<Int> {
        userCurrencyOtherCurrencyIndex = MutableLiveData()
        userCurrencyOtherCurrencyIndex.postValue(indexNotBaseCurrency)
        return userCurrencyOtherCurrencyIndex
    }
}
