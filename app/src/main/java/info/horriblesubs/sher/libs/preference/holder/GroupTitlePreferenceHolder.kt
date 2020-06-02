package info.horriblesubs.sher.libs.preference.holder

import android.content.Context
import android.view.View
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.PreferenceAdapter
import info.horriblesubs.sher.libs.preference.model.BasePreference

class GroupTitlePreferenceHolder(
    view: View,
    context: Context?,
    adapter: PreferenceAdapter
): BasePreferenceHolder(view, context, adapter) {
    private val textView: MaterialTextView? = itemView.findViewById(R.id.titleTextView)
    override fun onBindViewHolder(t: BasePreference<out Any>, position: Int) {
        textView?.text = t.title
    }
}