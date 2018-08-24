package app.itsyour.elegantstocks.feature.symbol

import app.itsyour.elegantstocks.RobolectricTest
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import app.itsyour.elegantstocks.feature.symbol.mocks.SYMBOL_APPLE_POSITION
import app.itsyour.elegantstocks.feature.symbol.mocks.fakeSymbols
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import kotlinx.android.synthetic.main.activity_symbol.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue



class SymbolActivityTest : RobolectricTest() {

    companion object {
        private val testScheduler = TestScheduler()
        @BeforeClass @JvmStatic
        fun setupClass() {
            RxJavaPlugins.setInitComputationSchedulerHandler { _ -> testScheduler }
        }
    }

    private lateinit var roboActivity: ActivityController<SymbolActivity>
    private lateinit var activity: SymbolActivity
    private lateinit var observer: TestObserver<SymbolContract.Action>
    private lateinit var symbols: List<Symbol>

    @Before
    fun setUp() {
        roboActivity = Robolectric.buildActivity(SymbolActivity::class.java)
        activity = roboActivity.create().start().resume().visible().get()
        observer = TestObserver<SymbolContract.Action>().also {
                activity.actions.subscribe(it) }
        symbols = fakeSymbols()
    }

    @Test
    fun whenSymbolsReceived_shouldBeAddedToAdapter() {
        activity.onStateReceived(SymbolContract.State.Symbols(symbols))

        assertTrue(activity.addnew_symbolsList.childCount == symbols.count())
    }

    @Test
    fun whenActivityLoads_shouldQueryAllSymbols() {
        activity.onStateReceived(SymbolContract.State.Symbols(symbols))

        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)

        val query = observer.values().first() as? SymbolContract.Action.Query
        assertTrue(query?.text == "")
    }

    @Test
    fun whenSymbolClicked_shouldBeAdded() {
        activity.onStateReceived(SymbolContract.State.Symbols(symbols))
        activity.addnew_symbolsList.getChildAt(SYMBOL_APPLE_POSITION).performClick()

        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)

        val query = observer.values()[0] as? SymbolContract.Action.Add
        assertEquals(query?.symbol, symbols[SYMBOL_APPLE_POSITION])
        observer.assertValueCount(2)
    }
}