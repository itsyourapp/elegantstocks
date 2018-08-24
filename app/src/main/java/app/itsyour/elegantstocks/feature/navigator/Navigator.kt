package app.itsyour.elegantstocks.feature.navigator

import io.reactivex.Observable

/**
 * Navigator is a way for child Fragment-s of Activity-s to observe actions
 * on their parent context.
 */
interface Navigator {
    val actions: Observable<Action>
    fun onStateReceived(state: State)

    sealed class Action {
        class Refresh : Action()
    }

    sealed class State {
        class Refreshed : State()
        class Refreshable(val canBeRefreshed: Boolean) : State()
    }
}
