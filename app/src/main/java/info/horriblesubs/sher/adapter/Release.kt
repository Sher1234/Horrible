package info.horriblesubs.sher.adapter

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.common.fromHtml

class ReleaseAdapter(click: ItemClick<Release>?): BaseAdapter<Release>(click) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Release> {
        return ReleaseHolder(this, itemClick, parent)
    }

    override fun getItemId(position: Int): Long {
        return listItems?.get(position)?.release?.toLong() ?: -1
    }

    override fun search(x: String?) {
        items = listItems?.searchRelease(x)
        size = items?.size ?:0
        notifyDataSetChanged()
    }
}

internal class ReleaseHolder (adapterX: ReleaseAdapter, bookmarkClick: ItemClick<Release>?, group: ViewGroup):
    BaseHolder<Release>(bookmarkClick, adapterX, R.layout._recycler_g, group) {

    private val textView: AppCompatTextView = itemView.findViewById(R.id.textView)
    private val fhd: View = itemView.findViewById(R.id.mark3)
    private val hd: View = itemView.findViewById(R.id.mark2)
    private val sd: View = itemView.findViewById(R.id.mark1)

    override fun load(t: Release?) {
        itemView.setOnClickListener{itemClick?.onClick(t)}
        textView.text = t?.release.fromHtml()
        if(t?.quality != null) {
            if (!t.quality[2]) fhd.visibility = View.GONE
            if (!t.quality[1]) hd.visibility = View.GONE
            if (!t.quality[0]) sd.visibility = View.GONE
        }
    }
}
