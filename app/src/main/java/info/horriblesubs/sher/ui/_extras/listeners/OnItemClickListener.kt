package info.horriblesubs.sher.ui._extras.listeners

import android.view.View

interface OnItemClickListener<T> {
    fun onItemLongClick(view: View, t: T?, position: Int) = onItemClick(view, t, position)
    fun invoke(view: View, t: T?, position: Int) = onItemClick(view, t, position)
    fun onItemClick(view: View, t: T?, position: Int) = Unit
}