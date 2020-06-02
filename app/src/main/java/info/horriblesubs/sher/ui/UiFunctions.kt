@file:JvmName("UiFunctions")
@file:Suppress("Unused")

package info.horriblesubs.sher.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition

const val FRAGMENT_STACK: String = "FRAGMENT_BACK_STACK"

const val PERMISSION_REQUEST = 9877

const val EXTRA_DATA = "EXTRA_DATA"

const val UPDATE_CHECK = 7895

val View?.enable: Unit get() {
    this?.isEnabled = true
}

val View?.disable: Unit get() {
    this?.isEnabled = false
}

val View?.visible: Unit get() {
    this?.visibility = View.VISIBLE
}

val View?.invisible: Unit get() {
    this?.visibility = View.INVISIBLE
}

val View?.gone: Unit get() {
    this?.visibility = View.GONE
}

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()


@MainThread
inline fun <reified VM : ViewModel> viewModels(
    storeOwner: ViewModelStoreOwner,
    noinline apply: VM.() -> VM = { this }
): Lazy<VM> = lazy {
    ViewModelProvider(storeOwner).get(VM::class.java).apply()
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    storeOwner: ViewModelStoreOwner = this,
    noinline apply: VM.() -> VM = { this }
): Lazy<VM> = lazy {
    ViewModelProvider(storeOwner).get(VM::class.java).apply()
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: () -> ViewModelProvider.Factory = {defaultViewModelProviderFactory}
): Lazy<VM> = lazy {
    ViewModelProvider(ownerProducer(), factoryProducer()).get(VM::class.java)
}

fun <VH: RecyclerView.ViewHolder, A: RecyclerView.Adapter<VH>> RecyclerView.setLinearLayoutAdapter(
    adapter: A?, orientation: Int = RecyclerView.VERTICAL
) {
    layoutManager = LinearLayoutManager(context, orientation, false)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
}

fun <VH: RecyclerView.ViewHolder, A: RecyclerView.Adapter<VH>> RecyclerView.setGridLayoutAdapter(
    adapter: A?, span: Int = 3
) {
    layoutManager = GridLayoutManager(context, span)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
}

fun RequestBuilder<Drawable>.into(imageView: ImageView?, progressBar: ProgressBar? = null) =
    into(object: ImageViewTarget<Drawable>(imageView) {

    override fun setResource(resource: Drawable?) {
        view.setImageDrawable(resource)
        progressBar?.gone
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)
        progressBar?.visible
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        progressBar?.gone
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        super.onLoadCleared(placeholder)
        progressBar?.gone
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        progressBar?.gone
    }
})

fun startBrowser(context: Context?, url: String, chooserText: String = "Select browser to open") {
    context?.startActivity(Intent.createChooser(Intent().apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }, chooserText))
}