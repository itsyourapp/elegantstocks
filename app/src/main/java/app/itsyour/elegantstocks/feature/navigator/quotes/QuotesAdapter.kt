package app.itsyour.elegantstocks.feature.navigator.quotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.utility.percentToScreen
import app.itsyour.elegantstocks.utility.random
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_quote_basic.view.*
import kotlinx.android.synthetic.main.item_quote_standard.view.*

class QuotesAdapter : RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    enum class Holder(val id: Int) {
        SIMPLE(0),
        REGULAR(1);

        companion object {
            fun from(findValue: Int): Holder = Holder.values().first { it.id == findValue }
        }
    }

    var snapshots: List<QuotesContract.Snapshot> = emptyList()
        set(value) {
            field = value
            randomizeSizes()
            notifyDataSetChanged()
        }

    private val subscriptions = CompositeDisposable()
    private val subject = PublishRelay.create<QuotesContract.Snapshot>()
    val selected: Observable<QuotesContract.Snapshot> = subject

    private val itemSizes = mutableMapOf<Int, Holder>()

    override fun getItemViewType(position: Int) = itemSizes[position]!!.id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (Holder.from(viewType)) {
            Holder.SIMPLE -> SimpleViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_quote_basic, parent, false))
            Holder.REGULAR -> RegularViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_quote_standard, parent, false))
        }
    }

    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            subscriptions.add(view.clicks().subscribe { subject.accept(snapshots[layoutPosition]) })
        }

        abstract fun bind(snapshot: QuotesContract.Snapshot)
    }

    inner class SimpleViewHolder(private val view: View) : ViewHolder(view) {
        override fun bind(snapshot: QuotesContract.Snapshot) {
            view.apply {
                item_quoteBasic_symbol.text = snapshot.quote.symbol
                item_quoteBasic_latestPrice.text = snapshot.quote.latestPrice.toPlainString()
                item_quoteBasic_change.text = snapshot.quote.change.toString()
                item_quoteBasic_changePercent.text = snapshot.quote.changePercent.percentToScreen()
            }
        }
    }

    inner class RegularViewHolder(private val view: View) : ViewHolder(view) {
        override fun bind(snapshot: QuotesContract.Snapshot) {
            view.apply {
                item_quoteDetail_symbol.text = snapshot.symbol.symbol
                item_quoteDetail_latestPrice.text = snapshot.quote.latestPrice.toPlainString()
                item_quoteDetail_change.text = snapshot.quote.change.toString()
                item_quoteDetail_changePercent.text = snapshot.quote.changePercent.percentToScreen()
                item_quoteDetail_chart.setChartEntries(snapshot.chartEntries)
            }
        }
    }

    override fun getItemCount(): Int = snapshots.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(snapshots[position])

    // This can be used later if a staggered grid is required.
    private fun randomizeSizes() {
        itemSizes.clear()
        for (i in 0..itemCount) {
            val random = (0..4).random()
            if (random < 4)
                itemSizes[i] = Holder.REGULAR
            else
                itemSizes[i] = Holder.SIMPLE
        }
    }
}