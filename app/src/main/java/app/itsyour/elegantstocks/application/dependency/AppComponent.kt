package app.itsyour.elegantstocks.application.dependency

import android.app.Application
import app.itsyour.elegantstocks.ElegantStocks
import app.itsyour.elegantstocks.application.dependency.scope.AppScoped
import app.itsyour.elegantstocks.feature.FeatureModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Exposes the root dependency graph for the entire application. The scope of the
 * component is conceptually from the beginning of app launch to app termination.
 */
@AppScoped
@Component(modules = [
        AppModule::class,
        AndroidSupportInjectionModule::class,
        FeatureModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: ElegantStocks)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}