package info.horriblesubs.sher.adapter

interface ItemClick<E> {
    fun onClick(e: E?)
}

interface BookmarkClick<E>: ItemClick<E> {
    fun onDelete(e: E?)
}