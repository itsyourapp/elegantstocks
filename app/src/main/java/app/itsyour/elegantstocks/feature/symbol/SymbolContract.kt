package app.itsyour.elegantstocks.feature.symbol

import androidx.annotation.StringRes
import app.itsyour.elegantstocks.Contract
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import io.reactivex.Observable

interface SymbolContract : Contract {

    interface View : Contract.View<State, Action> {
        override val actions: Observable<Action>

        override fun onStateReceived(state: State)
    }

    interface Presenter : Contract.Presenter<View>

    sealed class Action {
        class Query(val text: String) : Action()
        class Add(val symbol: Symbol) : Action()
    }

    sealed class State {
        class Loading : State()
        class Symbols(val symbols: List<Symbol>) : State()
        class Navigation(val route: Route) : State() {
            enum class Route {
                Quotes
            }
        }

        class Error(val type: Type = Type.General) : State() {
            enum class Type(@StringRes val msg: Int) {
                Network(R.string.quotes_error_network),
                EmptyList(R.string.quotes_error_noqueries),
                General(R.string.quotes_error_general)
            }
        }
    }
}
