package com.traphan.convertcurrency;

import androidx.room.Room;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import traphan.ren95.convertcurrency.database.AppDatabase;
import traphan.ren95.convertcurrency.database.dao.CurrencyDao;
import traphan.ren95.convertcurrency.database.entity.CurrencyEntity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4ClassRunner.class)
public class CurrencyDaoTest {

    private AppDatabase appDatabase;
    private CurrencyDao currencyDao;

    @Before
    public void initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), AppDatabase.class).build();
        currencyDao = appDatabase.currencyDao();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void currencyTest() {
        int size = Util.getRandomNumberInRange(10, 100);
        List<CurrencyEntity> currencyEntities = CurrencyTestHelper.getCurrencies(size);
        currencyDao.insertOrUpdateAllCurrency(currencyEntities).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            TestObserver<List<Integer>> countObserver = currencyDao.getCount().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test();
            countObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS);
            countObserver.assertNoErrors().assertComplete();
            Assert.assertTrue(currencyEntities.size() == countObserver.values().get(0).get(0));
            countObserver.dispose();
            TestObserver<List<CurrencyEntity>> currenciesObserver = currencyDao.getAllCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test();
            currenciesObserver.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS);
            currenciesObserver.assertNoErrors().assertComplete().assertValueCount(currencyEntities.size()).assertValue(currencyEntities);
            currenciesObserver.dispose();
            currencyDao.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                TestObserver<List<CurrencyEntity>> currenciesObserverSecond = currencyDao.getAllCurrency().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).test();
                currenciesObserverSecond.awaitCount(1).awaitDone(3, TimeUnit.MILLISECONDS);
                currenciesObserverSecond.assertNoErrors().assertComplete().assertEmpty();
            });
        });
    }
}
