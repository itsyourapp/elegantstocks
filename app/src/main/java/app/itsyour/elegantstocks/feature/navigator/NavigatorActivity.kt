package app.itsyour.elegantstocks.feature.navigator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.transaction
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.navigator.about.AboutFragment
import app.itsyour.elegantstocks.feature.navigator.news.NewsFragment
import app.itsyour.elegantstocks.feature.navigator.privacy.PrivacyFragment
import app.itsyour.elegantstocks.feature.navigator.quotes.QuotesFragment
import app.itsyour.elegantstocks.feature.symbol.SymbolActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_navigator.*

class NavigatorActivity :
        Navigator,
        NavigationView.OnNavigationItemSelectedListener,
        DaggerAppCompatActivity() {

    private val actionsSubject = PublishRelay.create<Navigator.Action>()
    override val actions: Observable<Navigator.Action> = actionsSubject

    private lateinit var bottomDrawerBehavior: BottomSheetBehavior<View>

    private var subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigator)
        setupBottomAppBar()
        setupSwipeRefresh()
        listenForAddedSymbol()
        listenForMenuSelection()
        displayQuotes()
    }

    private fun setupBottomAppBar() {
        setSupportActionBar(bottomAppBar)
        addBottomDrawToAppBar()
    }

    private fun setupSwipeRefresh() {
        navigator_swipeRefresh.setOnRefreshListener {
            actionsSubject.accept(Navigator.Action.Refresh())
        }
    }

    private fun addBottomDrawToAppBar() {
        bottomDrawerBehavior = BottomSheetBehavior.from<View>(bottomDrawer)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomAppBar.setNavigationOnClickListener{ _ ->
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED }
    }

    private fun listenForAddedSymbol() {
        subscriptions += fab.clicks().subscribe {
            SymbolActivity.navigateTo(this)
        }
    }

    private fun listenForMenuSelection() {
        navigation_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_quote -> displayQuotes()
            R.id.menu_news -> displayNews()
            R.id.menu_privacy -> displayLegal()
            R.id.menu_about -> displayAbout()
        }
        dismissMenu()
        return true
    }

    private fun displayQuotes() {
        navigator_swipeRefresh.isEnabled = true
        bottomAppBar.menu.findItem(R.id.menu_refresh)?.isVisible = true
        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.fragmentContainer, QuotesFragment.newInstance(), QuotesFragment.FRAGMENT_TAG)
        }
    }

    private fun displayNews() {
        navigator_swipeRefresh.isEnabled = true
        bottomAppBar.menu.findItem(R.id.menu_refresh)?.isVisible = true
        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.fragmentContainer, NewsFragment.newInstance(), NewsFragment.FRAGMENT_TAG)
        }
    }

    private fun displayLegal() {
        navigator_swipeRefresh.isEnabled = false
        bottomAppBar.menu.findItem(R.id.menu_refresh)?.isVisible = false
        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.fragmentContainer, PrivacyFragment.newInstance(), PrivacyFragment.FRAGMENT_TAG)
        }
    }

    private fun displayAbout() {
        navigator_swipeRefresh.isEnabled = false
        bottomAppBar.menu.findItem(R.id.menu_refresh)?.isVisible = false
        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.fragmentContainer, AboutFragment.newInstance(), AboutFragment.FRAGMENT_TAG)
        }
    }

    override fun onStateReceived(state: Navigator.State) {
        when (state) {
            is Navigator.State.Refreshed -> navigator_swipeRefresh.isRefreshing = false
            is Navigator.State.Refreshable -> {
                navigator_swipeRefresh.isEnabled = state.canBeRefreshed
                bottomAppBar.menu.findItem(R.id.menu_refresh)?.isVisible = state.canBeRefreshed
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_appbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                navigator_swipeRefresh.isRefreshing = true
                actionsSubject.accept(Navigator.Action.Refresh());
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (menuIsShowing())
            dismissMenu()
        else
            super.onBackPressed()
    }

    private fun menuIsShowing() =
        (bottomDrawerBehavior.state != BottomSheetBehavior.STATE_HIDDEN)

    private fun dismissMenu() {
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun hideFab() {
        fab.hide()
    }

    fun showFab() {
        fab.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}
