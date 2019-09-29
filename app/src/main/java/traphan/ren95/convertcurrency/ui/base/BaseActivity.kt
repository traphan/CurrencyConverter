package traphan.ren95.convertcurrency.ui.base

import androidx.appcompat.app.AppCompatActivity
import traphan.ren95.convertcurrency.ui.dialog.ExitDialog
import traphan.ren95.convertcurrency.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {
    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    override fun onBackPressed() {
        ExitDialog().show(this)
    }
}
