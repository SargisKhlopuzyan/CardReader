package app.sargis.khlopuzyan.cardreader.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.sargis.khlopuzyan.cardreader.di.factory.AppViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

/**
 * Created by Sargis Khlopuzyan, on 2/14/2020.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */
@Module(includes = [AppModule.ProvideViewModel::class])
abstract class AppModule {

    @Module
    class ProvideViewModel {
        @Provides
        fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory = AppViewModelFactory(providers)
    }

}