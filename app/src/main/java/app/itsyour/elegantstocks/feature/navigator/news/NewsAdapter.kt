package app.itsyour.elegantstocks.feature.navigator.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.navigator.news.model.MarketNews
import app.itsyour.elegantstocks.utility.iso8601ToScreen
import app.itsyour.elegantstocks.utility.toSpanned
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val newsSubject = PublishRelay.create<MarketNews>()
    val selectedMarketNews: Observable<MarketNews> = newsSubject

    var marketNews: List<MarketNews> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.clicks().subscribe { newsSubject.accept(marketNews[layoutPosition]) }
        }

        fun bind(marketNews: MarketNews) {
            view.apply {
                news_headline.text = marketNews.headline.toSpanned()
                news_date.text = marketNews.datetime.iso8601ToScreen()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = marketNews.count()

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.bind(marketNews[position])
    }
}