package traphan.ren95.convertcurrency.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import traphan.ren95.convertcurrency.ui.viewmodel.CurrencyViewModel
import traphan.ren95.convertcurrency.viewmodelfactory.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    /*
     * This method basically says
     * inject this object into a Map using the @IntoMap annotation,
     * with the  MovieListViewModel.class as key,
     * and a Provider that will build a MovieListViewModel
     * object.
     *
     * */

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyViewModel::class)
    protected abstract fun currencyViewModel(currencyViewModel: CurrencyViewModel): ViewModel



}