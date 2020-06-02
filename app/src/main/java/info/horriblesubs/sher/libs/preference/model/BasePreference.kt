package info.horriblesubs.sher.libs.preference.model

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import info.horriblesubs.sher.App

abstract class BasePreference<T> {
    protected val sharedPreferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(App.get())
    protected var summaryProvider: TextProvider<T>? = null
    var icon: Drawable? = null

    abstract val defaultValue: T
    abstract val key: String
    abstract val type: Int
    abstract var value: T

    protected fun setIcon(@DrawableRes icon: Int) {
        this.icon = ContextCompat.getDrawable(App.get(), icon)
    }

    var summary: String? = null
        get() = summaryProvider?.provideText(this) ?: field
    var title: String? = null

    protected fun setSummary(@StringRes id: Int) {
        summary = App.get().getString(id)
    }

    @Suppress("SameParameterValue")
    protected fun setTitle(@StringRes id: Int) {
        title = App.get().getString(id)
    }

    interface TextProvider<T> {

        fun provideText(preference: BasePreference<T>): String?
    }

    open fun migrate() = Unit

    companion object {
        const val SWITCH_PREFERENCE: Int = 4578
        const val LIST_PREFERENCE: Int = 9877
        const val GROUP_TITLE: Int = 7841
        const val DEFAULT: Int = 4981
    }
}