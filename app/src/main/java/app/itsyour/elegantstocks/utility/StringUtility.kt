package app.itsyour.elegantstocks.utility

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned

@SuppressLint("ObsoleteSdkInt")
fun String.toSpanned(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this)
    }
}