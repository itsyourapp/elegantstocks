package app.itsyour.elegantstocks.feature.chart.candle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.base.BaseActivity
import app.itsyour.elegantstocks.views.LoadingFrame
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_chart.*

class CandleActivity
    : BaseActivity<CandleContract.Presenter>(),
        CandleContract.View {

    companion object {
        private const val EXTRA_SYMBOL = "app.itsyour.elegantstocks::chartSymbol"

        fun navigateTo(context: Context, symbol: String) {
            val intent = Intent(context, CandleActivity::class.java)
            intent.putExtra(EXTRA_SYMBOL, symbol)
            context.startActivity(intent)
        }
    }

    private val relay = PublishRelay.create<CandleContract.Action>()
    override val actions: Observable<CandleContract.Action> = relay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        fragment_candle_chart.initialize()
    }

    override fun onStart() {
        super.onStart()
        relay.accept(CandleContract.Action.Select(intent.getStringExtra(EXTRA_SYMBOL)))
    }

    override fun onStateReceived(state: CandleContract.State) {
        when (state) {
            is CandleContract.State.ShowChart -> showChart(state)
        }
    }

    private fun showChart(action: CandleContract.State.ShowChart) {
        fragment_candle_loading.setState(LoadingFrame.SUCCESS)
        fragment_candle_chart.setData(action.entries)
    }

    override fun getLayoutId() = R.layout.activity_chart
}