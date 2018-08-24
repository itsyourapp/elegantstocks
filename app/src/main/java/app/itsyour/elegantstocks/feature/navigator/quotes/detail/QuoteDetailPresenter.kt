package app.itsyour.elegantstocks.feature.navigator.quotes.detail

import app.itsyour.elegantstocks.application.repository.MarketRepository
import app.itsyour.elegantstocks.feature.base.BasePresenter
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class QuoteDetailPresenter
    @Inject constructor(val repository: MarketRepository) :
        BasePresenter<QuoteDetailContract.View>(),
        QuoteDetailContract.Presenter {

    override fun onViewTaken() {
        subscriptions += view.actions.subscribe{
            when (it) {
                is QuoteDetailContract.Action.Select -> displaySymbol(it.symbol)
                is QuoteDetailContract.Action.Remove -> removeQuote(it.symbol)
                is QuoteDetailContract.Action.CandleChart -> candleChart()
                is QuoteDetailContract.Action.FilledChart -> filledChart()
                is QuoteDetailContract.Action.FilledCloseChart -> filledCloseChart()
                is QuoteDetailContract.Action.LineChart -> lineChart()
            }
        }
    }

    private fun displaySymbol(symbol: String) {
        subscriptions += repository.allQuotes.subscribe {
            view.onStateReceived(QuoteDetailContract.State.Display(it.first { it.symbol == symbol }))
        }
    }

    private fun removeQuote(symbol: String) {
        repository.delete(symbol)
        view.onStateReceived(QuoteDetailContract.State.Navigation(QuoteDetailContract.State.Navigation.Route.Quotes))
    }

    private fun candleChart() {
        view.onStateReceived(QuoteDetailContract.State.Navigation(QuoteDetailContract.State.Navigation.Route.CandleChart))
    }

    private fun filledChart() {
        view.onStateReceived(QuoteDetailContract.State.Navigation(QuoteDetailContract.State.Navigation.Route.FilledChart))
    }

    private fun filledCloseChart() {
        view.onStateReceived(QuoteDetailContract.State.Navigation(QuoteDetailContract.State.Navigation.Route.FilledCloseChart))
    }

    private fun lineChart() {
        view.onStateReceived(QuoteDetailContract.State.Navigation(QuoteDetailContract.State.Navigation.Route.LineChart))
    }
}