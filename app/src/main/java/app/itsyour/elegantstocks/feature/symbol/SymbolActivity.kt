package app.itsyour.elegantstocks.feature.symbol

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.R.id.addnew_symbolsList
import app.itsyour.elegantstocks.R.id.searchView
import app.itsyour.elegantstocks.feature.base.BaseActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_symbol.*
import java.util.concurrent.TimeUnit

class SymbolActivity : BaseActivity<SymbolContract.Presenter>(), SymbolContract.View {

    companion object {
        fun navigateTo(context: Context) {
            context.startActivity(Intent(context, SymbolActivity::class.java))
        }
    }

    private val adapter = SymbolAdapter()

    override val actions: Observable<SymbolContract.Action>
        get() = Observable.merge(
            adapter.selectedSymbol.map { SymbolContract.Action.Add(it) },
            searchObservable())

    private fun searchObservable() = RxSearchView.queryTextChangeEvents(searchView)
            .distinctUntilChanged()
            .debounce(75, TimeUnit.MILLISECONDS)
            .map { it.queryText().toString() }
            .map { SymbolContract.Action.Query(it) }
            .observeOn(AndroidSchedulers.mainThread())

    override fun getLayoutId() = R.layout.activity_symbol

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSymbolsList()
    }

    private fun setupSymbolsList() {
        addnew_symbolsList.layoutManager = LinearLayoutManager(this)
        addnew_symbolsList.adapter = adapter
    }

    override fun onStateReceived(state: SymbolContract.State) {
        when (state) {
            is SymbolContract.State.Loading -> { }
            is SymbolContract.State.Symbols -> adapter.symbols = state.symbols
            is SymbolContract.State.Navigation -> navigateTo(state.route)
        }
    }

    private fun navigateTo(route: SymbolContract.State.Navigation.Route) {
        when (route) {
            SymbolContract.State.Navigation.Route.Quotes -> finish()
        }
    }
}