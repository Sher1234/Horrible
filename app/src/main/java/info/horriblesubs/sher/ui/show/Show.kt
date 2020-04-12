package info.horriblesubs.sher.ui.show

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.common.GoogleAds
import info.horriblesubs.sher.common.LoadingListener
import info.horriblesubs.sher.dialog.LoadingDialog
import info.horriblesubs.sher.ui.show.detail.ShowDetail
import info.horriblesubs.sher.ui.show.releases.Releases
import info.horriblesubs.sher.ui.show.view.ReleaseView

class Show: AppCompatActivity(), LoadingListener {
    
    private var dialog: LoadingDialog? = null
    private lateinit var model: ShowVM

    fun viewReleases(release: Release? = null) {
        swapFragment(if (release == null) ShowKey.ReleaseList else ShowKey.Release)
        model.release.value = release
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Constants.theme()
        setContentView(R.layout._c_activity)
        val link = intent?.getStringExtra("link")
        if (link.isNullOrEmpty()) {
            Toast.makeText(this, "App error", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        model = ViewModelProvider(this).get(ShowVM::class.java)
        model.initialize(this, link)
        swapFragment(ShowKey.Detail)
        GoogleAds.INTERSTITIAL.ad()
        model.refresh()
    }

    private fun swapFragment(key: ShowKey) {
        val oldFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        val newFragment = supportFragmentManager.findFragmentByTag(key.name)
        if (oldFragment == newFragment && newFragment != null) return
        supportFragmentManager.beginTransaction().replace(R.id.fragment, key.fragment, key.name)
            .addToBackStack(key.stackName).commit()
    }

    override fun onBackPressed() {
        Log.e("TAG", "onBackPressed: " + supportFragmentManager.backStackEntryCount)
        if(supportFragmentManager.backStackEntryCount <= 1) finish()
        else supportFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        model.stop()
        stop()
    }

    override fun start() {
        if (dialog == null)
            dialog = LoadingDialog(this)
        dialog?.show()
    }

    override fun stop() {
        dialog?.dismiss()
        dialog = null
    }

    private enum class ShowKey {
        Detail, ReleaseList, Release;
        val stackName: String = "Fragment Show Stack"
        val fragment: Fragment get() = when (this) {
            ReleaseList -> Releases()
            Release -> ReleaseView()
            Detail -> ShowDetail()
        }
    }
}