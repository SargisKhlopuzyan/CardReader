package app.sargis.khlopuzyan.cardreader.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.sargis.khlopuzyan.cardreader.di.annotation.ViewModelKey
import app.sargis.khlopuzyan.cardreader.ui.MainFragment
import app.sargis.khlopuzyan.cardreader.ui.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by Sargis Khlopuzyan, on 2/14/2020.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */

@Module(includes = [MainModule.ProvideViewModel::class])
interface MainModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    fun bind(): MainFragment

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        fun provideMainViewModel(
        ): ViewModel = MainViewModel()
    }

    @Module
    class InjectViewModel {
        @Provides
        fun provideMainViewModel(
            factory: ViewModelProvider.Factory,
            target: MainFragment
        ) = ViewModelProvider(target, factory)[MainViewModel::class.java]
    }

}