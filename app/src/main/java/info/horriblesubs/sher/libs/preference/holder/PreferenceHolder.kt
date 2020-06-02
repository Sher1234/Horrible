package info.horriblesubs.sher.libs.preference.holder

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.PreferenceAdapter
import info.horriblesubs.sher.libs.preference.model.BasePreference

class PreferenceHolder(
    view: View,
    context: Context?,
    adapter: PreferenceAdapter
): BasePreferenceHolder(view, context, adapter) {
    private val subtitleTextView: MaterialTextView? = itemView.findViewById(R.id.subtitleTextView)
    private val titleTextView: MaterialTextView? = itemView.findViewById(R.id.titleTextView)
    private val iconView: AppCompatImageView? = itemView.findViewById(R.id.iconView)
    override fun onBindViewHolder(t: BasePreference<out Any>, position: Int) {
        itemView.setOnClickListener { adapter.listener?.onPreferenceChange(t, position) }
        subtitleTextView?.text = t.summary
        iconView?.setImageDrawable(t.icon)
        titleTextView?.text = t.title
    }
}