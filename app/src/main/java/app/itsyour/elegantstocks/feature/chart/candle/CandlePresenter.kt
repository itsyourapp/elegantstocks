package app.itsyour.elegantstocks.feature.chart.candle

import app.itsyour.elegantstocks.feature.base.BasePresenter
import app.itsyour.elegantstocks.feature.chart.model.ChartsInteractor
import com.github.mikephil.charting.data.CandleEntry
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject

class CandlePresenter
@Inject constructor(private val interactor: ChartsInteractor)
    : BasePresenter<CandleContract.View>(),
        CandleContract.Presenter {

    override fun onViewTaken() {
        view.actions.flatMap(::onAction).subscribe(view::onStateReceived)
    }

    private fun onAction(action: CandleContract.Action): Observable<CandleContract.State> {
        return when (action) {
            is CandleContract.Action.Select -> selectChart(action)
        }
    }

    private fun selectChart(action: CandleContract.Action.Select): Observable<CandleContract.State> =
            interactor.fetchChart(action.symbol, ChartsInteractor.Companion.Range.THREE_MONTHS)
                    .flatMapIterable { it }
                    .zipWith(Observable.range(0, Int.MAX_VALUE))
                    .map {
                        CandleEntry(
                                it.second.toFloat(),
                                it.first.high.toFloat(),
                                it.first.low.toFloat(),
                                it.first.open.toFloat(),
                                it.first.close.toFloat()) }
                    .toList()
                    .toObservable()
                    .map { CandleContract.State.ShowChart(it) }
}