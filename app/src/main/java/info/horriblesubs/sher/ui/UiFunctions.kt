@file:JvmName("UiFunctions")
@file:Suppress("Unused")

package info.horriblesubs.sher.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.core.app.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
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
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager

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
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline apply: VM.() -> VM = { this }
): Lazy<VM> = lazy {
    ViewModelProvider(ownerProducer()).get(VM::class.java).apply()
}

//@MainThread
//inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModels(
//    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
//    noinline factoryProducer: () -> ViewModelProvider.Factory = {defaultViewModelProviderFactory}
//): Lazy<VM> = lazy {
//    ViewModelProvider(ownerProducer(), factoryProducer()).get(VM::class.java)
//}

@MainThread
inline fun <reified VDB: ViewDataBinding> LifecycleOwner?.viewBindings(
    inflater: LayoutInflater, layoutId: Int, group: ViewGroup?,
    lifecycleOwner: LifecycleOwner? = this,
    crossinline apply: VDB.() -> Unit = {}
): VDB {
    val vdb = DataBindingUtil.inflate<VDB>(inflater, layoutId, group, false)
    vdb.lifecycleOwner = lifecycleOwner
    vdb.apply()
    return vdb
}

@MainThread
inline fun <reified VDB: ViewDataBinding> viewBindings(layoutId: Int, group: ViewGroup): VDB =
    DataBindingUtil.inflate(LayoutInflater.from(group.context), layoutId, group, false)

@MainThread
inline fun <reified VDB: ViewDataBinding> ComponentActivity.viewBindings(
    @LayoutRes layoutId: Int, crossinline apply: VDB.() -> Unit = {}
): VDB {
    val vdb = DataBindingUtil.setContentView<VDB>(this, layoutId)
    vdb.lifecycleOwner = this
    vdb.apply()
    return vdb
}

@MainThread
inline fun <reified VDB: ViewDataBinding> ComponentActivity.viewBinding(
    @LayoutRes layoutId: Int, crossinline apply: VDB.() -> Unit = {}
): Lazy<VDB> = lazy {
    val vdb = DataBindingUtil.setContentView<VDB>(this, layoutId)
    vdb.lifecycleOwner = this
    vdb.apply()
    vdb
}

fun <VH: RecyclerView.ViewHolder, A: RecyclerView.Adapter<VH>> RecyclerView.setLinearLayoutAdapter(
    adapter: A?, orientation: Int = RecyclerView.VERTICAL
) {
    layoutManager = LinearLayoutManager(context, orientation, false)
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
}

fun <VH: RecyclerView.ViewHolder, A: RecyclerView.Adapter<VH>> RecyclerView.setFlexLayoutAdapter(adapter: A?) {
    layoutManager = FlexboxLayoutManager(context).apply { alignItems = AlignItems.CENTER }
    itemAnimator = DefaultItemAnimator()
    this.adapter = adapter
}

fun <VH: RecyclerView.ViewHolder, A: RecyclerView.Adapter<VH>> RecyclerView.setGridLayoutAdapter(
    adapter: A?, span: Int = 3, orientation: Int = RecyclerView.VERTICAL
) {
    layoutManager = GridLayoutManager(context, span).apply { this.orientation = orientation }
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

fun Context?.toast(msg: String) {
    this?.let { Toast.makeText(it, msg, Toast.LENGTH_SHORT).show() }
}

fun startBrowser(context: Context?, url: String, chooserText: String = "Select browser to open") {
    context?.startActivity(Intent.createChooser(Intent().apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }, chooserText))
}

@Suppress("FunctionName")
inline fun Runnable(crossinline block: Runnable.() -> Unit): Runnable =
    object : Runnable {
        override fun run() { block() }
    }
