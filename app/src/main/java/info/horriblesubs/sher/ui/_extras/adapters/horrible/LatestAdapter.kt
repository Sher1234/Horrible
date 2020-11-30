package info.horriblesubs.sher.ui._extras.adapters.horrible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.database.inLibrary
import info.horriblesubs.sher.data.subsplease.api.model.ItemLatest
import info.horriblesubs.sher.data.subsplease.api.model.imageUrl
import info.horriblesubs.sher.libs.preference.prefs.MarkedPreference
import info.horriblesubs.sher.ui._extras.listeners.OnItemClickListener
import info.horriblesubs.sher.ui.gone
import info.horriblesubs.sher.ui.visible

class LatestAdapter(
    private val listener: OnItemClickListener<ItemLatest>
): RecyclerView.Adapter<LatestAdapter.Holder>() {

    private var listClone: MutableList<ItemLatest> = mutableListOf()
    private var list: MutableList<ItemLatest> = mutableListOf()
    private var isMarkedAvailable = MarkedPreference.value

    override fun getItemCount() = list.size

    override fun getItemId(position: Int) = -1L

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(group.context).inflate(R.layout.recycler_item_c, group, false),
        this@LatestAdapter
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position], position)
    }

    fun reset(list: List<ItemLatest>? = null) {
        isMarkedAvailable = MarkedPreference.value
        this.list.clear()
        if (list.isNullOrEmpty()) {
            this.list = listClone
        } else {
            this.listClone.clear()
            this.list = list.toMutableList()
            this.listClone = list.toMutableList()
        }
        notifyDataSetChanged()
    }

    class Holder(
        view: View,
        private val adapter: LatestAdapter
    ): RecyclerView.ViewHolder(view) {

        private val release: AppCompatTextView? = itemView.findViewById(R.id.textView2)
        private val image: AppCompatImageView? = itemView.findViewById(R.id.imageView)
        private val title: AppCompatTextView? = itemView.findViewById(R.id.textView1)
        private val marker: View? = itemView.findViewById(R.id.marked)
        private val fhd: View? = itemView.findViewById(R.id.mark3)
        private val hd: View? = itemView.findViewById(R.id.mark2)
        private val sd: View? = itemView.findViewById(R.id.mark1)

        fun onBindToViewHolder(t: ItemLatest, position: Int) {
            image?.let { Glide.with(it).load(t.imageUrl).transform(CircleCrop()).into(it) }
            itemView.setOnClickListener { adapter.listener.onItemClick(it, t, position) }
            release?.text = t.episode.parseAsHtml()
            title?.text = t.show.parseAsHtml()
            t.downloads.forEachIndexed { i, it ->
                if (i == 0) sd?.visibility = if (it.res.contains("360") || it.res.contains("480")) View.VISIBLE else View.GONE
                if (i != 2) hd?.visibility = if (it.res.contains("720", true)) View.VISIBLE else View.GONE
                fhd?.visibility = if (it.res.contains("1080", true)) View.VISIBLE else View.GONE
            }
            if (adapter.isMarkedAvailable)
                inLibrary(t.page) {
                    if (this) marker?.visible else marker?.gone
                }
        }
    }
}