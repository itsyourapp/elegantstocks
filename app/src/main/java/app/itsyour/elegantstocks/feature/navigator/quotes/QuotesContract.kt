package app.itsyour.elegantstocks.feature.navigator.quotes

import app.itsyour.elegantstocks.Contract
import app.itsyour.elegantstocks.feature.navigator.quotes.model.Quote
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import com.github.mikephil.charting.data.Entry
import io.reactivex.Observable

interface QuotesContract : Contract {

    interface View : Contract.View<State, Action> {
        override val actions: Observable<Action>
        override fun onStateReceived(state: State)
    }

    interface Presenter : Contract.Presenter<View>

    sealed class Action {
        class Refresh : Action()
        class Select(val snapshot: Snapshot) : Action()
    }

    sealed class State {
        class Snapshots(val snapshots: List<Snapshot>) : State()

        class NoSymbols : State()

        class Refreshed: State()

        class Error(val type: Type = Type.General) : State() {
            enum class Type {
                General
            }
        }

        sealed class Navigate : State() {
            class Details(var snapshot: Snapshot) : Navigate()
        }
    }

    data class Snapshot(
            var symbol: Symbol,
            var quote: Quote,
            var chartEntries: List<Entry>)
}
