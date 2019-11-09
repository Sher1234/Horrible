package info.horriblesubs.sher.dialog

import android.app.Dialog
import android.content.Context
import androidx.annotation.LayoutRes
import info.horriblesubs.sher.R

abstract class BaseDialog(context: Context, @LayoutRes layout: Int): Dialog(context, R.style.Theme_Dialog) {
    final override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }
    init {setContentView(layout)}
}