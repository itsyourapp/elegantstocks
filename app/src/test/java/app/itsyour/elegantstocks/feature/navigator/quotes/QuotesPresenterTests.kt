package app.itsyour.elegantstocks.feature.navigator.quotes

import app.itsyour.elegantstocks.MockitoTest
import app.itsyour.elegantstocks.application.repository.MarketRepository
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import app.itsyour.elegantstocks.feature.navigator.quotes.model.Quote
import app.itsyour.elegantstocks.utility.toFlowable
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import java.net.UnknownHostException
import kotlin.test.assertEquals

class QuotesPresenterTests : MockitoTest() {

    @Mock
    private lateinit var interactor: QuotesInteractor
    @Mock
    private lateinit var view: QuotesContract.View
    @Mock
    private lateinit var repository: MarketRepository

    @Test
    fun whenUserHasNoSymbols_shouldShowEmptyState() {
        whenever(repository.allSymbols).thenReturn(Flowable.just(emptyList()))
        whenever(view.actions).thenReturn(Observable.empty<QuotesContract.Action>())

        QuotesPresenter(interactor, repository)
                .takeView(view)

        verify(view).onStateReceived(any<QuotesContract.State.NoSymbols>())
    }

    @Test
    fun whenQuoteFetchSucceeds_shouldShowQuotes() {
        val symbolsWithQuotes = listOf(fakeSymbolWithQuote())

        whenever(interactor.fetchSnapshots(ArgumentMatchers.anyList<Symbol>()))
                .thenReturn(Single.just(symbolsWithQuotes))
        whenever(repository.allSymbols).thenReturn(Flowable.just(listOf(fakeSymbol())))
        whenever(view.actions).thenReturn(Observable.empty<QuotesContract.Action>())

        QuotesPresenter(interactor, repository)
                .takeView(view)

        argumentCaptor<QuotesContract.State.Snapshots>().apply {
            verify(view, times(2)).onStateReceived(capture())
            assertEquals(firstValue.snapshots, symbolsWithQuotes, "View did not receive the interactor's list.")
        }
    }

    @Test
    fun whenQuoteFetchFails_shouldShowErrorState() {
        whenever(view.actions).thenReturn(Observable.empty<QuotesContract.Action>())
        whenever(interactor.fetchSnapshots(any())).thenReturn(
                Single.error(UnknownHostException()))
        whenever(repository.allSymbols).thenReturn(Flowable.just(listOf(fakeSymbol())))
        whenever(interactor.symbols).thenReturn(Single.just(listOf(fakeSymbol())))

        QuotesPresenter(interactor, repository)
                .takeView(view)

        argumentCaptor<QuotesContract.State.Error>().apply {
            verify(view, times(2)).onStateReceived(capture())
            assertEquals(firstValue.type, QuotesContract.State.Error.Type.General, "View did not receive an error state.")
        }
    }

    @Test
    fun whenFirstQuoteAddedToEmptyList_shouldShowQuote() {
        val symbolsWithQuotes = listOf(fakeSymbolWithQuote())
        val symbols = BehaviorSubject.createDefault<List<Symbol>>(emptyList())

        whenever(repository.allSymbols).thenReturn(symbols.toFlowable())
        whenever(interactor.fetchSnapshots(ArgumentMatchers.anyList<Symbol>())).thenReturn(
                Single.just(symbolsWithQuotes))
        whenever(view.actions).thenReturn(Observable.empty())

        QuotesPresenter(interactor, repository)
                .takeView(view)

        verify(view).onStateReceived(any<QuotesContract.State.NoSymbols>())

        symbols.onNext(listOf(fakeSymbol()))

        argumentCaptor<QuotesContract.State.Snapshots>().apply {
            verify(view, times(3)).onStateReceived(capture())
            assertEquals(secondValue.snapshots, symbolsWithQuotes, "View did not receive the interactor's list.")
        }
    }

    private fun fakeSymbolWithQuote() =
            QuotesContract.Snapshot(fakeSymbol(), fakeQuote(), emptyList())

    private fun fakeQuote(): Quote =
            Quote(
                    symbol = "AAPL",
                    companyName = "Apple",
                    primaryExchange = "",
                    sector = "",
                    calculationPrice = "",
                    open = 0.toBigDecimal(),
                    openTime = 0L,
                    close = 0.toBigDecimal(),
                    closeTime = 0L,
                    high = 0.toBigDecimal(),
                    low = 0.toBigDecimal(),
                    latestPrice = 0.toBigDecimal(),
                    latestSource = "",
                    latestTime = "",
                    latestUpdate = 0L,
                    latestVolume = 0L,
                    delayedPrice = 0.toBigDecimal(),
                    delayedPriceTime = 0L,
                    previousClose = 0.toBigDecimal(),
                    change = 0F,
                    changePercent = 0F,
                    marketCap = 0L,
                    peRatio = 0F,
                    week52High = 0.toBigDecimal(),
                    week52Low = 0.toBigDecimal(),
                    ytdChange = 0F)

    private fun anotherFakeQuote(): Quote =
            Quote(
                    symbol = "GOOG",
                    companyName = "Google",
                    primaryExchange = "",
                    sector = "",
                    calculationPrice = "",
                    open = 0.toBigDecimal(),
                    openTime = 0L,
                    close = 0.toBigDecimal(),
                    closeTime = 0L,
                    high = 0.toBigDecimal(),
                    low = 0.toBigDecimal(),
                    latestPrice = 0.toBigDecimal(),
                    latestSource = "",
                    latestTime = "",
                    latestUpdate = 0L,
                    latestVolume = 0L,
                    delayedPrice = 0.toBigDecimal(),
                    delayedPriceTime = 0L,
                    previousClose = 0.toBigDecimal(),
                    change = 0F,
                    changePercent = 0F,
                    marketCap = 0L,
                    peRatio = 0F,
                    week52High = 0.toBigDecimal(),
                    week52Low = 0.toBigDecimal(),
                    ytdChange = 0F)

    private fun fakeSymbol(): Symbol =
            Symbol("AAPL", "", "", false, "", "")

    private fun anotherFakeSymbol(): Symbol =
            Symbol("GOOG", "", "", false, "", "")
}
