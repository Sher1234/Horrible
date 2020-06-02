package info.horriblesubs.sher.libs.preference.prefs

import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.model.BasePreference

object GithubPreference: BasePreference<String>() {

    override val defaultValue = App.get().getString(R.string.github_link)
    override val key: String get() = "github-link"
    override val type: Int get() = DEFAULT
    override var value = defaultValue

    init {
        setSummary(R.string.github_link)
        setTitle(R.string.github)
    }
}