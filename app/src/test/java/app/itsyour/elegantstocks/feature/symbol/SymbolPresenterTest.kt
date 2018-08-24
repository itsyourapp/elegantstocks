package app.itsyour.elegantstocks.feature.symbol

import app.itsyour.elegantstocks.MockitoTest
import app.itsyour.elegantstocks.RxImmediateSchedulerRule
import app.itsyour.elegantstocks.application.repository.MarketRepository
import app.itsyour.elegantstocks.feature.navigator.quotes.QuotesInteractor
import app.itsyour.elegantstocks.feature.symbol.mocks.SYMBOL_APPLE_POSITION
import app.itsyour.elegantstocks.feature.symbol.mocks.SYMBOL_GOOGLE_POSITION
import app.itsyour.elegantstocks.feature.symbol.mocks.fakeSymbols
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mock
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SymbolPresenterTest : MockitoTest() {

    companion object {
        @ClassRule @JvmField
        val rxRule = RxImmediateSchedulerRule()
    }

    @Mock
    private lateinit var view: SymbolContract.View
    @Mock
    private lateinit var interactor: QuotesInteractor
    @Mock
    private lateinit var repository: MarketRepository

    @Test
    fun whenPresenterStarts_shouldPutUiInLoadingState() {
        whenever(view.actions).thenReturn(Observable.empty())
        whenever(interactor.symbols).thenReturn(Single.just(emptyList()))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        verify(view, times(2)).onStateReceived(any<SymbolContract.State.Loading>())
    }

    @Test
    fun whenSymbolsComeBack_shouldUpdateUiWithSymbols() {
        val fakeSymbols = fakeSymbols()

        whenever(view.actions).thenReturn(Observable.empty())
        whenever(interactor.symbols).thenReturn(Single.just(fakeSymbols))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        argumentCaptor<SymbolContract.State.Symbols>().apply {
            verify(view, times(2)).onStateReceived(capture())
            assertEquals(fakeSymbols, secondValue.symbols,
                    "View did not receive the correct symbols list")
        }
    }

    @Test
    fun whenSymbolsError_shouldShowUiNetworkError() {
        whenever(view.actions).thenReturn(Observable.empty())
        whenever(interactor.symbols).thenReturn(Single.error(mock<HttpException>()))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        argumentCaptor<SymbolContract.State.Error>().apply {
            verify(view, times(2)).onStateReceived(capture())
            assertEquals(secondValue.type, SymbolContract.State.Error.Type.Network)
        }
    }

    @Test
    fun whenUnknownErrorPropogates_shouldShowUiGeneralError() {
        whenever(view.actions).thenReturn(Observable.error(Throwable("Unknown Error")))
        whenever(interactor.symbols).thenReturn(Single.just(fakeSymbols()))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        argumentCaptor<SymbolContract.State.Error>().apply {
            verify(view, times(2)).onStateReceived(capture())
            assertEquals(secondValue.type, SymbolContract.State.Error.Type.General)
        }
    }

    /**
     * Test demonstrates TestSchedulers approach.
     * See the following test for PublishSubject approach.
     */
    @Test
    fun whenUserQueryDoesNotMatchSymbols_ShouldShowEmptyList() {
        val scheduler = TestScheduler()
        val userDelay = 5L
        val actions = Observable
                .just<SymbolContract.Action>(SymbolContract.Action.Query("Q"))
                .delay(userDelay, TimeUnit.MICROSECONDS, scheduler)

        whenever(view.actions).thenReturn(actions)
        whenever(interactor.symbols).thenReturn(Single.just(fakeSymbols()))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        scheduler.advanceTimeBy(userDelay, TimeUnit.MICROSECONDS)

        argumentCaptor<SymbolContract.State>().apply {
            verify(view, times(3)).onStateReceived(capture())
            assertTrue(secondValue is SymbolContract.State.Symbols)
            assertEquals((thirdValue as SymbolContract.State.Error).type,
                    SymbolContract.State.Error.Type.EmptyList)
        }
    }

    /**
     * Test demonstrates PublishSubject approach.
     * See the previous test for TestScheduler approach.
     */
    @Test
    fun whenUserQueryMatchesSymbol_ShouldShowMatchingList() {
        val symbols = fakeSymbols()
        val actions = PublishSubject.create<SymbolContract.Action>()

        whenever(view.actions).thenReturn(actions)
        whenever(interactor.symbols).thenReturn(Single.just(symbols))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        actions.onNext(SymbolContract.Action.Query("GO"))

        argumentCaptor<SymbolContract.State.Symbols>().apply {
            verify(view, times(3)).onStateReceived(capture())
            assertEquals(2, secondValue.symbols.size,
                    "List should be the complete symbol list.")
            assertEquals(1, thirdValue.symbols.size,
                    "List should be truncated to one matching symbol.")
            assertTrue(thirdValue.symbols.contains(symbols[SYMBOL_GOOGLE_POSITION]))
            assertFalse(thirdValue.symbols.contains(symbols[SYMBOL_APPLE_POSITION]))
        }
    }

    @Test
    fun whenSymbolSelected_shouldBePersisted() {
        val actions = PublishSubject.create<SymbolContract.Action>()
        val symbols = fakeSymbols()

        whenever(view.actions).thenReturn(actions)
        whenever(interactor.symbols).thenReturn(Single.just(symbols))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        actions.onNext(SymbolContract.Action.Add(symbols[SYMBOL_GOOGLE_POSITION]))

        verify(repository).insert(eq(symbols[SYMBOL_GOOGLE_POSITION]))
    }

    @Test
    fun whenSymbolSelected_shouldNavigateToQuotesScreen() {
        val actions = PublishSubject.create<SymbolContract.Action>()
        val symbols = fakeSymbols()

        whenever(view.actions).thenReturn(actions)
        whenever(interactor.symbols).thenReturn(Single.just(symbols))

        SymbolPresenter(interactor, repository)
                .takeView(view)

        actions.onNext(SymbolContract.Action.Add(symbols[SYMBOL_APPLE_POSITION]))

        argumentCaptor<SymbolContract.State.Navigation>().apply {
            verify(view, times(3)).onStateReceived(capture())
            assertEquals(SymbolContract.State.Navigation.Route.Quotes, thirdValue.route)
        }
    }
}