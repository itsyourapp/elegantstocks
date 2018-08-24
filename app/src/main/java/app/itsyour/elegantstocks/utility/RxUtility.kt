package app.itsyour.elegantstocks.utility

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

fun <T> Observable<T>.toFlowable(): Flowable<T> = toFlowable(BackpressureStrategy.LATEST)

fun <T> BehaviorSubject<T>.toFlowable(): Flowable<T> = toFlowable(BackpressureStrategy.LATEST)

fun <T> applyObservableSchedulers(observeOn: Scheduler = AndroidSchedulers.mainThread()): ObservableTransformer<T, T>
    = ObservableTransformer { it.subscribeOn(Schedulers.io()).observeOn(observeOn) }

fun <T> applyFlowableSchedulers(observeOn: Scheduler = AndroidSchedulers.mainThread()): FlowableTransformer<T, T>
    = FlowableTransformer { it.subscribeOn(Schedulers.io()).observeOn(observeOn) }