package com.traphan.currencyconverter.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.base.BaseActivity
import com.traphan.currencyconverter.ui.base.BaseFragment
import com.traphan.currencyconverter.ui.viewmodel.CurrencyViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.ArrayList
import javax.inject.Inject

class Splash : BaseActivity(), HasSupportFragmentInjector {
    private lateinit var viewModel: CurrencyViewModel

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    private val REQUEST_EXTERNAL_STORAGE = 666
    private var permissionLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun verifyStoragePermissions(activity: AppCompatActivity) {
        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val notGranted = ArrayList<String>()
        // Check if we have write permission
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permission)
            }
        }
        val notGrantedArr = arrayOfNulls<String>(notGranted.size)
        if (!notGranted.isEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                notGranted.toTypedArray(),
                REQUEST_EXTERNAL_STORAGE
            )
        } else {
            permissionLiveData.value = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_splash)
        permissionLiveData.value = false
        verifyStoragePermissions(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrencyViewModel::class.java)
        viewModel.getCountUserCurrency().observe(this, Observer {
            if (it) {
                startNextAction(CurrencyCalculationFragment.newInstance())
            } else {
                startNextAction(BaseCurrencyFragment.newInstance())
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === REQUEST_EXTERNAL_STORAGE) {
            for (i in 0 until grantResults.size) {
                val permission = permissions[i]
                var isPermitted = grantResults[i] === PackageManager.PERMISSION_GRANTED
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    verifyStoragePermissions(this)
                } else {
                    permissionLiveData.value = true
                }
            }
        }
    }

    private fun startNextAction(fragment: BaseFragment) {
        permissionLiveData.observe(this, Observer {
            if(it) {
                supportFragmentManager.beginTransaction().replace(R.id.base_background, fragment).addToBackStack(null).commit()
            }
        })
    }
}
