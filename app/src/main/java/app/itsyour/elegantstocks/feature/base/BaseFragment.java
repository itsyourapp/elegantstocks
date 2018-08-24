package app.itsyour.elegantstocks.feature.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.itsyour.elegantstocks.Contract;
import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.CompositeDisposable;

/**
 * A helpful base class for Fragments that contain a Contract.Presenter,
 * a UI-specific subscription bag, layout inflating and view taking/dropping.
 * Injections are automatic with DaggerFragment.
 *
 * TODO: Move to Kotlin
 */
public abstract class BaseFragment<P extends Contract.Presenter> extends DaggerFragment {

    @Inject
    protected P presenter;

    /**
     * A useful subscription bag (only to be used for view-specific streams).
     * BasePresenter has it's own subscription bag for presentater-specific streams.
     */
    protected CompositeDisposable subscriptions = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.takeView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.dropView();
        subscriptions.dispose();
    }

    @LayoutRes
    abstract protected int getLayoutId();

    /**
     * An implementation that, by default, does not consume a back press.
     * Meant to be overriden by fragments that could consume back presses.
     */
    public boolean onBackPressed() {
        return false;
    }
}
