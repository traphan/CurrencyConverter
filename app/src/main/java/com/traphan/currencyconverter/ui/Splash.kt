package com.traphan.currencyconverter.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.traphan.currencyconverter.R
import com.traphan.currencyconverter.ui.base.BaseActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.ArrayList
import javax.inject.Inject



class Splash : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    private val REQUEST_EXTERNAL_STORAGE = 666

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
            startNextAction()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_splash)
        verifyStoragePermissions(this)
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
                    startNextAction()
                }
            }
        }
    }

    private fun startNextAction() {
        val baseCurrencyFragment = BaseCurrencyFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.base_background, baseCurrencyFragment).addToBackStack(null).commit()
    }

}
