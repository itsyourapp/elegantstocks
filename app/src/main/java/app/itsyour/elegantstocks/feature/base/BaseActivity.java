package app.itsyour.elegantstocks.feature.base;

import android.os.Bundle;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.VisibleForTesting;
import app.itsyour.elegantstocks.Contract;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

/**
 * A helpful base class for Activities that contain a Contract.Presenter,
 * a UI-specific subscription bag, layout inflating and view taking/dropping.
 * Injections are automatic with DaggerAppCompatActivity.
 *
 * TODO: Move to Kotlin
 */
public abstract class BaseActivity<P extends Contract.Presenter> extends DaggerAppCompatActivity {

    @VisibleForTesting
    @Inject
    public P presenter;

    /**
     * A useful subscription bag (only to be used for view-specific streams).
     * BasePresenter has it's own subscription bag for presentater-specific streams.
     */
    protected CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.takeView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.dropView();
    }

    @LayoutRes
    abstract protected int getLayoutId();
}