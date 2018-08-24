package app.itsyour.elegantstocks.feature.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

abstract class BaseAdapter<M, S> : RecyclerView.Adapter<BaseAdapter<M, S>.ViewHolder>() {
    private val relay = PublishRelay.create<S>()
    val actions: Observable<S>
        get() = relay

    var values: List<M> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(values[position])

    override fun getItemCount(): Int = values.size

    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(model: M)
    }
}
