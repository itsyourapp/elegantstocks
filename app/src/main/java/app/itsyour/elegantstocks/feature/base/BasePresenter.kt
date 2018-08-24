package app.itsyour.elegantstocks.feature.base

import app.itsyour.elegantstocks.Contract
import io.reactivex.disposables.CompositeDisposable

/**
 * A useful base class for Presenters with a subscription bag and
 * hook methods for when views are taken or dropped.
 */
abstract class BasePresenter<V : Contract.View<*, *>> : Contract.Presenter<V> {

    /**
     * A subscription bag that is automatically disposed on dropView().
     */
    protected val subscriptions: CompositeDisposable = CompositeDisposable()

    /**
     * A reference to the view so this Presenter can push State
     */
    protected lateinit var view: V

    /**
     * View taking happens during onStart().
     * onViewTaken is a hook method for inheriters to override.
     */
    final override fun takeView(view: V) {
        this.view = view
        onViewTaken()
    }
    open fun onViewTaken() {}

    /**
     * View dropping happens during onStop().
     * onViewDropped is a hook method for inheriters to override.
     */
    final override fun dropView() {
        subscriptions.clear()
        onViewDropped()
    }
    open fun onViewDropped() {}
}