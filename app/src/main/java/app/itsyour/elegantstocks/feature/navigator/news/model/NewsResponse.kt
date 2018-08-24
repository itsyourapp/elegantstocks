package app.itsyour.elegantstocks.feature.navigator.news.model

import com.google.gson.annotations.Expose

data class NewsResponse(
        @Expose var marketNews: List<MarketNews>)