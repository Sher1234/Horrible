package info.horriblesubs.sher.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.inflate

class DownloadAdapter (private val itemClick: ItemClick<String>? = null): RecyclerView.Adapter<DownloadHolder>() {

    private var keys: List<String>? = null
    var downloads: Map<String, String?>? = null
        set(value) {
            keys = value?.keys?.toList()
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadHolder {
        return DownloadHolder(itemClick, parent)
    }

    override fun onBindViewHolder(holder: DownloadHolder, position: Int) {
        holder.load(keys?.get(position), downloads?.get(keys?.get(position)))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return downloads?.size ?: 0
    }
}

class DownloadHolder (private val itemClick: ItemClick<String>?, group: ViewGroup):
    RecyclerView.ViewHolder(group.inflate(R.layout._recycler_h)) {

    private val button: MaterialButton = itemView.findViewById(R.id.button)

    fun load(key: String?, link: String?) {
        button.setOnClickListener{itemClick?.onClick(link)}
        button.isEnabled = !link.isNullOrEmpty()
        button.text = key.fromHtml()
    }
}
