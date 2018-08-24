package app.itsyour.elegantstocks.application.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Provides a helpful HTTP data cache that can be added with Retrofit builders.
 */
class CacheInterceptor : Interceptor {

    companion object {
        const val NO_CACHE_HEADER = "no-cache: true"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        val maxAge = 60 * 30 // Read from cache for 30 minutes.
        val response = originalResponse.newBuilder()
        if ("true" != request.header("no-cache"))
            response.header("Cache-Control", "public, max-age=$maxAge")
        response.removeHeader("no-cache")
        return response.build()
    }
}