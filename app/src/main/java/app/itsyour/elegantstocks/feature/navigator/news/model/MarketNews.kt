package app.itsyour.elegantstocks.feature.navigator.news.model

import com.google.gson.annotations.Expose

data class MarketNews(
        @Expose var datetime: String,
        @Expose var headline: String,
        @Expose var source: String,
        @Expose var url: String,
        @Expose var summary: String,
        @Expose var related: String)