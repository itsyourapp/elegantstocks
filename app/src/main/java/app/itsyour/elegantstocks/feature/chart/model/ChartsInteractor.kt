package app.itsyour.elegantstocks.feature.chart.model

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChartsInteractor
    @Inject constructor(private val api: ChartsApi) {

    companion object {
        enum class Range(val toString: String) {
            ONE_MONTH("1m"),
            THREE_MONTHS("3m")
        }
    }

    fun fetchChart(symbol: String, range: Range): Observable<List<ChartEntry>> =
         api.fetchChart(symbol, range.toString)
                 .toObservable()
}