package app.itsyour.elegantstocks.feature.chart.candle

import app.itsyour.elegantstocks.application.dependency.scope.ActivityScoped
import app.itsyour.elegantstocks.feature.chart.model.ChartsInteractor
import dagger.Provides

@dagger.Module
abstract class CandleModule {

    @dagger.Module
    companion object {
        @Provides @ActivityScoped @JvmStatic
        fun candlePresenter(interactor: ChartsInteractor): CandleContract.Presenter
                = CandlePresenter(interactor)
    }
}