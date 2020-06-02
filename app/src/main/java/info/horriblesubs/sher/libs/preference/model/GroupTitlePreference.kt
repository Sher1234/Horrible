package info.horriblesubs.sher.libs.preference.model

import info.horriblesubs.sher.BuildConfig

class GroupTitlePreference(title: String): BasePreference<String>() {

    override val defaultValue = BuildConfig.BUILD_TIME
    override val key: String get() = title ?: ""
    override val type: Int get() = GROUP_TITLE
    override var value = defaultValue

    init { this.title = title }
}