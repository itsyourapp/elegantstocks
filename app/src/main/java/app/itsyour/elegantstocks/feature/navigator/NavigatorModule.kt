package app.itsyour.elegantstocks.feature.navigator

import app.itsyour.elegantstocks.application.dependency.scope.FragmentScoped
import app.itsyour.elegantstocks.feature.navigator.about.AboutFragment
import app.itsyour.elegantstocks.feature.navigator.news.NewsModule
import app.itsyour.elegantstocks.feature.navigator.privacy.PrivacyFragment
import app.itsyour.elegantstocks.feature.navigator.quotes.QuotesModule
import dagger.android.ContributesAndroidInjector

@dagger.Module(includes = [
     QuotesModule::class,
     NewsModule::class
])
abstract class NavigatorModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun legalFragment(): PrivacyFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun aboutFragment(): AboutFragment
}