package info.horriblesubs.sher.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import info.horriblesubs.sher.api.horrible.model.ItemBase
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.ui.main.settings.KeySettings

abstract class BaseAdapter<E>(val itemClick: ItemClick<E>?): Adapter<BaseHolder<E>>() {

    protected var listItems: MutableList<E>?
    protected var items: MutableList<E>?
    protected var size: Int?

    init {
        listItems = null
        items = listItems?.toMutableList()
        size = items?.size
    }

    override fun onBindViewHolder(holder: BaseHolder<E>, position: Int) {
        holder.load(items?.get(position))
    }

    open fun reset(listItems: MutableList<E>?) {
        this.listItems = listItems
        size = listItems?.size
        items = listItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return size?:0
    }

    open fun search(x: String?) {}

    open fun reset() {
        items = listItems
        size = listItems?.size
        notifyDataSetChanged()
    }
}

abstract class BaseHolder<E> internal constructor(
    internal val itemClick: ItemClick<E>? = null,
    internal val adapter: BaseAdapter<E>,
    @LayoutRes val layout: Int,
    group: ViewGroup
) : RecyclerView.ViewHolder(group.inflate(layout)) {
    internal val isMarkerEnabled: Boolean = Constants.value(KeySettings.MarkedFav) as Boolean
    abstract fun load(t: E?)
}

fun <E: ItemBase> MutableList<E>.search(x: String?): MutableList<E>? {
    return if (isNullOrEmpty() || x.isNullOrBlank()) this
    else {
        val list: MutableList<E> = mutableListOf()
        forEach {
            if ((it.title?:"").contains(x, true))
                list.add(it)
        }
        list
    }
}

fun MutableList<Release>.searchRelease(x: String?): MutableList<Release>? {
    return if (isNullOrEmpty() || x.isNullOrBlank()) this
    else {
        val list: MutableList<Release> = mutableListOf()
        forEach {
            if ((it.release?:"").contains(x, true))
                list.add(it)
        }
        list
    }
}