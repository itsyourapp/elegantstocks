package app.itsyour.elegantstocks

import io.reactivex.Observable

/**
 * Model-view-intent approach. Views expose their actions to Presenters via
 * streams of Action-s. Views receive their State-s from Presenters via
 * ordinary method push. Views and Presenters form the typical MVP pattern.
 */
interface Contract {

    interface View<in State, Action> {
        val actions: Observable<Action>
            get() = Observable.empty<Action>()

        fun onStateReceived(state: State)
    }

    interface Presenter<in View> {
        fun takeView(view: View)
        fun dropView()
    }
}
