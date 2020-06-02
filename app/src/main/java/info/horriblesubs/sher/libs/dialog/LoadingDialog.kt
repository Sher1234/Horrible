package info.horriblesubs.sher.libs.dialog

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import info.horriblesubs.sher.R
import java.util.*

class LoadingDialog(context: Context): BaseDialog(context, R.layout._lib_dialog_loading) {
    private fun setLoadingGif() {
        val name = context.resources
            .getStringArray(R.array.asset_names)[Random().nextInt(6)]?:"gif_01.gif"
        val gifImageView = AppCompatImageView(context)
        findViewById<FrameLayout>(R.id.layout).addView(
            gifImageView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        Glide.with(context).load(Uri.parse("file:///android_asset/$name"))
            .placeholder(R.drawable.app_placeholder).into(gifImageView)
    }
    init {
        setCancelable(false)
        setLoadingGif()
    }
}