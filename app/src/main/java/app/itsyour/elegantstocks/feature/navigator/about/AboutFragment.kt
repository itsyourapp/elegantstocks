package app.itsyour.elegantstocks.feature.navigator.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.navigator.NavigatorActivity
import app.itsyour.elegantstocks.utility.toSpanned
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_about.view.*
import kotlinx.android.synthetic.main.fragment_privacy.*
import javax.inject.Inject

class AboutFragment @Inject constructor() : DaggerFragment() {

    companion object {
        const val FRAGMENT_TAG = "aboutFragment"
        fun newInstance() = AboutFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_about_webview.loadUrl("file:///android_asset/About.html")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideFab()
    }

    private fun hideFab() {
        (activity as NavigatorActivity).hideFab()
    }
}