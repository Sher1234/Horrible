package info.horriblesubs.sher.ui.c

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.RepositoryResult
import info.horriblesubs.sher.data.subsplease.api.model.ItemRelease
import info.horriblesubs.sher.databinding.CActivityBinding
import info.horriblesubs.sher.functions.GoogleAds
import info.horriblesubs.sher.libs.dialog.LoadingDialog
import info.horriblesubs.sher.libs.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.listeners.OnBackPressedListener
import info.horriblesubs.sher.ui.c.detail.ShowDetailFragment
import info.horriblesubs.sher.ui.c.releases.ReleasesFragment


class ShowActivity: AppCompatActivity() {

    private val binding by viewBinding<CActivityBinding>(R.layout.c_activity)

    companion object {
        fun startShowActivity(context: Context?, link: String, executeAtEnd: () -> Unit = {}) {
            context?.startActivity(getShowActivityIntent(context, link))
            executeAtEnd()
        }
        fun getShowActivityIntent(context: Context?, link: String): Intent {
            return Intent(context, ShowActivity::class.java).apply {
                putExtra(EXTRA_DATA, link)
            }
        }
    }

    private var errorDialog: NetworkErrorDialog? = null
    private var loadingDialog: LoadingDialog? = null

    private val model by viewModels<ShowModel> {
        val link = intent?.getStringExtra(EXTRA_DATA)
        if (link.isNullOrBlank()) {
            this@ShowActivity.toast("Internal app error!!!")
            finish()
        } else showLink = link
        this
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(binding.root)
        errorDialog = NetworkErrorDialog(this)
        model.episodes.observe(this) { onLoadListener(it) }
        model.detail.observe(this) { onLoadListener(it) }
        GoogleAds(this).apply {
            getBannerAd(this@ShowActivity, binding.adBannerLayout)
            getInterstitialAd(this@ShowActivity)
        }
        when(model.itemId) {
            "show-releases" -> ReleasesFragment()
            else -> ShowDetailFragment()
        }.let { onChangeFragment(it) }
    }

    private fun <T> onLoadListener(it: RepositoryResult<T>?) {
        when(it?.status) {
            RepositoryResult.SUCCESS -> onSetLoading(false)
            RepositoryResult.LOADING -> onSetLoading(true)
            RepositoryResult.FAILURE -> onSetError()
            null -> onSetError()
        }
    }

    private fun onSetError() {
        onSetLoading(false)
        if (model.showLink.isNullOrEmpty()) toast("Undefined URL!")
        else errorDialog?.show("https://horriblesubs.info/show/${model.showLink}")
    }

    private fun onSetLoading(b: Boolean) {
        loadingDialog = if (b) {
            onSetLoading(false)
            LoadingDialog(this).apply { show() }
        } else {
            loadingDialog?.dismiss()
            null
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (fragment !is OnBackPressedListener || fragment.onBackPressed(this)) {
            if(supportFragmentManager.backStackEntryCount <= 1) finish()
            else supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        loadingDialog?.dismiss()
        errorDialog?.dismiss()
        model.stopServerCall
        super.onDestroy()
    }

    fun onChangeFragment(fragment: Fragment, t: ItemRelease? = null) {
        val newFragment = supportFragmentManager.findFragmentByTag(fragment.javaClass.name)
        val visibleFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (visibleFragment != newFragment || visibleFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment, fragment.javaClass.name)
                .addToBackStack(FRAGMENT_STACK).commit()
            model.itemId = fragment.javaClass.name
        }
        model.sharedItem = t
    }
}