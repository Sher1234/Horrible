package info.horriblesubs.sher.ui.e

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui._extras.listeners.OnBackPressedListener
import info.horriblesubs.sher.ui.e.character_staff.CharacterStaffFragment
import info.horriblesubs.sher.ui.e.home.AnimeHomeFragment
import kotlinx.android.synthetic.*


class AnimeMalActivity: AppCompatActivity() {

    companion object {
        fun startAnimeMalActivity(context: Context?, id: Int, executeAtEnd: () -> Unit = {}) {
            context?.startActivity(getAnimeMalActivityIntent(context, id))
            executeAtEnd()
        }
        private fun getAnimeMalActivityIntent(context: Context?, id: Int): Intent {
            return Intent(context, AnimeMalActivity::class.java).apply {
                putExtra(EXTRA_DATA, id)
            }
        }
    }

    private val model by viewModels<AnimeModel> {
        val id = intent?.getIntExtra(EXTRA_DATA, -1) ?: -1
        if (id < 1) {
            this@AnimeMalActivity.toast("Internal app error!!!")
            finish()
        } else this.id = id
        this
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.c_activity)
/*        TODO("Enable Ads")
 *        GoogleAds(this).apply {
 *            getBannerAd(this@AnimeMalActivity, adBannerLayout)
 *            getInterstitialAd(this@AnimeMalActivity)
 *        }
*/
        when(model.itemId) {
            "character-staff" -> CharacterStaffFragment()
            else -> AnimeHomeFragment()
        }.let {
            onChangeFragment(it)
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
        clearFindViewByIdCache()
        super.onDestroy()
    }

    fun <T: BaseFragment> onChangeFragment(fragment: T) {
        val visibleFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        val newFragment = supportFragmentManager.findFragmentByTag(fragment.name)
        if (visibleFragment != newFragment || visibleFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment, fragment.name)
                .addToBackStack(FRAGMENT_STACK).commit()
            model.itemId = fragment.name
        }
    }
}