package traphan.ren95.convertcurrency.database.datasourceimpl

import traphan.ren95.convertcurrency.database.datasource.CurrencyLocal
import traphan.ren95.convertcurrency.database.dao.CurrencyDao
import traphan.ren95.convertcurrency.database.entity.CurrencyEntity
import traphan.ren95.convertcurrency.database.entity.CurrencyJoinImage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class CurrencyLocalImpl(private val currencyDao: CurrencyDao) :
    CurrencyLocal {

    override fun loadCurrencyJoinImage(currencyIds: List<String>): Single<List<CurrencyJoinImage>> {
        return currencyDao.loadCurrencyJoinImage(currencyIds)
    }

    override fun loadAllCurrencyJoinImage(): Observable<List<CurrencyJoinImage>> {
        return currencyDao.loadAllCurrencyJoinImage()
    }

    override fun insertOrUpdateAllCurrency(currencies : List<CurrencyEntity>): Completable {
        return currencyDao.insertOrUpdateAllCurrency(currencies)
    }

    override fun deleteAllCurrency(): Completable {
        return currencyDao.deleteAll()
    }

    override fun getAllCurrency(): Observable<List<CurrencyEntity>> {
        return currencyDao.getAllCurrency()
    }

    override fun getCount(): Observable<List<Int>> {
        return currencyDao.getCount()
    }
}