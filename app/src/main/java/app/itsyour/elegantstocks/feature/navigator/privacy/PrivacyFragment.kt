package app.itsyour.elegantstocks.feature.navigator.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.itsyour.elegantstocks.R
import app.itsyour.elegantstocks.feature.navigator.NavigatorActivity
import app.itsyour.elegantstocks.utility.toSpanned
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_privacy.*
import kotlinx.android.synthetic.main.fragment_privacy.view.*
import javax.inject.Inject

class PrivacyFragment @Inject constructor() : DaggerFragment() {

    companion object {
        const val FRAGMENT_TAG = "legalFragment"
        fun newInstance() = PrivacyFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_privacy_webview.loadUrl("file:///android_asset/Privacy.html")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideFab()
    }

    private fun hideFab() {
        (activity as NavigatorActivity).hideFab()
    }
}