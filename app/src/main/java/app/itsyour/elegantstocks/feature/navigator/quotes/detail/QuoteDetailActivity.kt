package app.itsyour.elegantstocks.feature.navigator.quotes.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.ViewCompat
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.base.BaseActivity
import app.itsyour.elegantstocks.feature.chart.candle.CandleActivity
import app.itsyour.elegantstocks.feature.navigator.quotes.model.Quote
import app.itsyour.elegantstocks.utility.currencyToScreen
import app.itsyour.elegantstocks.utility.epochToScreen
import app.itsyour.elegantstocks.views.SheetMorphingFab
import com.google.android.material.appbar.AppBarLayout
import com.gordonwong.materialsheetfab.MaterialSheetFab
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_quote_detail.*

class QuoteDetailActivity :
        BaseActivity<QuoteDetailContract.Presenter>(),
        QuoteDetailContract.View,
        AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val EXTRA_SYMBOL = "app.itsyour.elegantstocks::quoteDetailSymbol"
        private const val PERCENTAGE_TO_SHOW_IMAGE = 20

        fun navigateTo(context: Context, symbol: String) {
            val intent = Intent(context, QuoteDetailActivity::class.java)
            intent.putExtra(EXTRA_SYMBOL, symbol)
            context.startActivity(intent)
        }
    }

    private val relay = PublishRelay.create<QuoteDetailContract.Action>()
    override val actions: Observable<QuoteDetailContract.Action> = relay

    private var maxScrollSize = 0
    private var isImageHidden = false
    private lateinit var morphingFab: MaterialSheetFab<SheetMorphingFab>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbarLayout()
        setupMorphingFab()
        listenForRemove()
        listenForChartSelections()
    }

    private fun setupToolbarLayout() {
        quotedetail_toolbar.setNavigationOnClickListener { onBackPressed() }
        quotedetail_appbarlayout.addOnOffsetChangedListener(this)
    }

    private fun setupMorphingFab() {
        morphingFab = MaterialSheetFab(quotedetail_chartfab, activity_quote_fab_sheet, activity_quote_detail_overlay,
                getColor(R.color.surfaceDark), getColor(R.color.secondary))
    }

    private fun listenForRemove() {
        subscriptions += quotedetail_removefab.clicks().subscribe {
            relay.accept(QuoteDetailContract.Action.Remove(intent.getStringExtra(EXTRA_SYMBOL)))
        }
    }

    private fun listenForChartSelections() {
        subscriptions += activity_quote_fab_sheet_item_candle.clicks().subscribe {
            relay.accept(QuoteDetailContract.Action.CandleChart) }
        subscriptions += activity_quote_fab_sheet_item_close.clicks().subscribe {
            relay.accept(QuoteDetailContract.Action.FilledCloseChart) }
        subscriptions += activity_quote_fab_sheet_item_filled.clicks().subscribe {
            relay.accept(QuoteDetailContract.Action.FilledChart) }
        subscriptions += activity_quote_fab_sheet_item_line.clicks().subscribe {
            relay.accept(QuoteDetailContract.Action.LineChart) }
    }

    override fun onStart() {
        super.onStart()
        relay.accept(QuoteDetailContract.Action.Select(intent.getStringExtra(EXTRA_SYMBOL)))
    }

    override fun onStateReceived(state: QuoteDetailContract.State) {
        when (state) {
            is QuoteDetailContract.State.Display -> displayQuote(state.quote)
            is QuoteDetailContract.State.Navigation -> navigateTo(state.route)
        }
    }

    private fun navigateTo(route: QuoteDetailContract.State.Navigation.Route) {
        when (route) {
            QuoteDetailContract.State.Navigation.Route.Quotes -> finish()
            QuoteDetailContract.State.Navigation.Route.LineChart ->
                CandleActivity.navigateTo(this, intent.getStringExtra(EXTRA_SYMBOL))
        }
    }

    private fun displayQuote(quote: Quote) {
        quotedetail_toolbarlayout.title = quote.companyName
        quotedetail_symbol.text = quote.symbol
        quotedetail_sector.text = quote.sector
        quotedetail_exchange.text = quote.primaryExchange
        quotedetail_price.text = quote.latestPrice.currencyToScreen()
        quotedetail_open.text = quote.open.currencyToScreen()
        quotedetail_close.text = quote.close.currencyToScreen()
        quotedetail_volume.text = quote.latestVolume.toString()
        quotedetail_high.text = quote.high?.currencyToScreen()
        quotedetail_low.text = quote.low?.currencyToScreen()
        quotedetail_latestupdate.text = quote.latestUpdate.epochToScreen()
    }

    override fun getLayoutId(): Int = R.layout.activity_quote_detail

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.totalScrollRange

        val currentScrollPercentage = Math.abs(i) * 100 / maxScrollSize

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!isImageHidden) {
                isImageHidden = true
                ViewCompat.animate(quotedetail_removefab).scaleY(0f).scaleX(0f).start()
                ViewCompat.animate(quotedetail_chartfab).scaleY(0f).scaleX(0f).start()
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (isImageHidden) {
                isImageHidden = false
                ViewCompat.animate(quotedetail_removefab).scaleY(1f).scaleX(1f).start()
                ViewCompat.animate(quotedetail_chartfab).scaleY(1f).scaleX(1f).start()
            }
        }
    }

    override fun onBackPressed() {
        if (morphingFab.isSheetVisible) morphingFab.hideSheet()
        else super.onBackPressed()
    }
}
