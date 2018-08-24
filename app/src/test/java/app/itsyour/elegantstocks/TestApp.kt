package app.itsyour.elegantstocks

import app.itsyour.elegantstocks.application.dependency.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TestApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder().application(this).build()
                    .also { it.inject(this) }
}