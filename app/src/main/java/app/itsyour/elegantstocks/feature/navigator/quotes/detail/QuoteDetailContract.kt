package app.itsyour.elegantstocks.feature.navigator.quotes.detail

import app.itsyour.elegantstocks.Contract
import app.itsyour.elegantstocks.feature.navigator.quotes.model.Quote
import io.reactivex.Observable

interface QuoteDetailContract : Contract {

    interface View : Contract.View<State, Action> {
        override val actions: Observable<Action>

        override fun onStateReceived(state: State)
    }

    interface Presenter : Contract.Presenter<View>

    sealed class Action {
        class Select(var symbol: String) : Action()
        class Remove(var symbol: String) : Action()
        object LineChart : Action()
        object FilledChart : Action()
        object FilledCloseChart : Action()
        object CandleChart : Action()
    }

    sealed class State {
        class Display(var quote: Quote) : State()

        class Navigation(val route: Route) : State() {
            enum class Route {
                Quotes,
                LineChart,
                FilledChart,
                FilledCloseChart,
                CandleChart
            }
        }
    }
}
