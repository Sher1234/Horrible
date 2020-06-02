package info.horriblesubs.sher.libs.preference.listeners

import info.horriblesubs.sher.libs.preference.model.BasePreference

interface OnPreferenceChangeListener {
    fun <T> onPreferenceChange(preference: BasePreference<T>, position: Int)
}