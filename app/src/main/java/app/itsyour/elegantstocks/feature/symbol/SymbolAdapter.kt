package app.itsyour.elegantstocks.feature.symbol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_symbol.view.*

class SymbolAdapter : RecyclerView.Adapter<SymbolAdapter.ViewHolder>() {

    private val symbolSubject = PublishRelay.create<Symbol>()
    val selectedSymbol: Observable<Symbol> = symbolSubject

    var symbols: List<Symbol> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.clicks().subscribe { symbolSubject.accept(symbols[layoutPosition]) }
        }

        fun bind(symbol: Symbol) {
            view.apply {
                title.text = symbol.name
                subtitle.text = symbol.symbol
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_symbol, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = symbols.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(symbols[position])
}