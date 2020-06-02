package info.horriblesubs.sher.libs.preference.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.libs.preference.PreferenceAdapter
import info.horriblesubs.sher.libs.preference.model.BasePreference

abstract class BasePreferenceHolder (
    view: View,
    protected val context: Context?,
    protected val adapter: PreferenceAdapter
): RecyclerView.ViewHolder(view) {
    abstract fun onBindViewHolder(t: BasePreference<out Any>, position: Int)
}