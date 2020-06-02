package info.horriblesubs.sher.service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.EXTRA_DATA
import info.horriblesubs.sher.ui.b.MainActivity
import info.horriblesubs.sher.ui.c.ShowActivity
import java.io.Serializable

class IntentHandler: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Log.e(packageName, "onCreate: $action: $data")
        Log.e(packageName, "onCreate: EncodedPath: ${data?.encodedPath}")
        Log.e(packageName, "onCreate: PathSegments: ${data?.pathSegments}")
        when(data?.pathSegments?.size) {
            0 -> startActivity(R.id.explore, MainActivity::class.java)
            1 -> {
                val id = when(data.pathSegments[0]) {
                    "bookmarked", "bookmark", "favourites" -> R.id.library
                    "schedule", "release-schedule" -> R.id.schedule
                    "shows", "current-season" -> R.id.shows
                    else -> R.id.explore
                }
                startActivity(id, MainActivity::class.java)
            }
            2 -> when(data.pathSegments[0]) {
                    "show", "shows", "bookmarked", "bookmark", "favourites" ->
                        ShowActivity.startShowActivity(this, data.pathSegments[1]) { finish() }
                    else -> startActivity(R.id.library, MainActivity::class.java)
                }
            else -> startActivity(R.id.library, MainActivity::class.java)
        }
    }

    private fun <E: Serializable, T> startActivity(s: E, clazz: Class<T>) {
        val intent = Intent(this, clazz)
        intent.putExtra(EXTRA_DATA, s)
        startActivity(intent)
        finish()
    }
}