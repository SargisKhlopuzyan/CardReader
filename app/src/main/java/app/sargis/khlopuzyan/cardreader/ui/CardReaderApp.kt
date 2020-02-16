package app.sargis.khlopuzyan.cardreader.ui

import android.content.Context
import app.sargis.khlopuzyan.cardreader.BuildConfig
import app.sargis.khlopuzyan.cardreader.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

/**
 * Created by Sargis Khlopuzyan, on 2/14/2020.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@fcc.am)
 */
class CardReaderApp : DaggerApplication() {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        context = this

        return DaggerAppComponent
            .factory()
            .create(this)
    }

    companion object {

        private lateinit var context: Context

        fun getContext() = context
    }
}