package info.horriblesubs.sher.dialog

import android.content.Context
import android.widget.FrameLayout
import info.horriblesubs.sher.R
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.util.*

class LoadingDialog(context: Context): BaseDialog(context, R.layout._dialog_a) {
    private fun setLoadingGif() {
        val name = context.resources
            .getStringArray(R.array.asset_names)[Random().nextInt(6)]?:"gif_01.gif"
        val drawable = GifDrawable(context.assets, name)
        val imageView = GifImageView(context)
        imageView.setImageDrawable(drawable)
        findViewById<FrameLayout>(R.id.frameLayout).addView(imageView)
    }
    init {
        setCancelable(false)
        setLoadingGif()
    }
}