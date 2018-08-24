package app.itsyour.elegantstocks.feature.navigator.news

import app.itsyour.elegantstocks.Contract
import app.itsyour.elegantstocks.feature.navigator.news.model.MarketNews
import io.reactivex.Observable

interface NewsContract : Contract {

    interface View : Contract.View<State, Action> {
        override val actions: Observable<NewsContract.Action>
        override fun onStateReceived(state: NewsContract.State)
    }

    interface Presenter : Contract.Presenter<View>

    sealed class Action {
        class Select(val marketNews: MarketNews) : Action()
        class Refresh : Action()
    }

    sealed class State {
        class News(val marketNews: List<MarketNews>) : State()

        class Error(val type: Type = Type.General) : State() {
            enum class Type {
                General
            }
        }

        class Link(val url: String) : State()

        class Refreshed : State()
    }
}