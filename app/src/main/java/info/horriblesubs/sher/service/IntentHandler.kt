package info.horriblesubs.sher.service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import info.horriblesubs.sher.R
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.ui.main.MainActivity
import info.horriblesubs.sher.ui.show.Show
import java.io.Serializable

class IntentHandler: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Constants.theme()
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Log.e(packageName, "onCreate: $action: $data")
        Log.e(packageName, "onCreate: EncodedPath: ${data?.encodedPath}")
        Log.e(packageName, "onCreate: PathSegments: ${data?.pathSegments}")
        when(data?.pathSegments?.size) {
            0 -> startActivity(R.id.explore, MainActivity::class.java)
            1 -> when(data.pathSegments[0]) {
                    "bookmarked", "bookmark", "favourites" -> startActivity(R.id.bookmarked, MainActivity::class.java)
                    "schedule", "release-schedule" -> startActivity(R.id.schedule, MainActivity::class.java)
                    "shows", "current-season" -> startActivity(R.id.shows, MainActivity::class.java)
                    "settings" -> startActivity(R.id.settings, MainActivity::class.java)
                    else -> startActivity(R.id.explore, MainActivity::class.java)
                }
            2 -> when(data.pathSegments[0]) {
                    "show", "shows", "bookmarked", "bookmark", "favourites" ->
                        startActivity(data.pathSegments[1], Show::class.java)
                    else -> startActivity(R.id.bookmarked, MainActivity::class.java)
                }
            else -> startActivity(R.id.bookmarked, MainActivity::class.java)
        }
    }

    private fun <E: Serializable, T> startActivity(s: E, clazz: Class<T>) {
        val intent = Intent(this, clazz)
        intent.putExtra("link", s)
        startActivity(intent)
        finish()
    }
}