package app.itsyour.elegantstocks.feature.chart.candle

import app.itsyour.elegantstocks.Contract
import com.github.mikephil.charting.data.CandleEntry
import io.reactivex.Observable

interface CandleContract : Contract {

    interface View : Contract.View<State, Action> {
        override val actions: Observable<Action>
        override fun onStateReceived(state: State)
    }

    interface Presenter : Contract.Presenter<View>

    sealed class Action {
        class Select(var symbol: String) : Action()
    }

    sealed class State {
        class ShowChart(val entries: List<CandleEntry>) : State()
    }
}