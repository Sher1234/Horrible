package info.horriblesubs.sher.ui.a

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import info.horriblesubs.sher.R
import info.horriblesubs.sher.databinding.AActivityBinding
import info.horriblesubs.sher.ui.*
import info.horriblesubs.sher.ui.b.MainActivity
import android.Manifest.permission as Permission
import androidx.core.content.ContextCompat.checkSelfPermission as isAvailable

class ScreenA: AppCompatActivity(), InstallStateUpdatedListener {

    private val manager by lazy { AppUpdateManagerFactory.create(this) }

    private val message = MutableLiveData<String>().apply { value = "Checking for permissions..." }

    private val dots by lazy {
        arrayListOf(binding.loadingDotA, binding.loadingDotB, binding.loadingDotC,
            binding.loadingDotD, binding.loadingDotE)
    }

    private var loading = 0

    private val binding by viewBinding<AActivityBinding>(R.layout.a_activity) {
        message = this@ScreenA.message
    }

    private fun onLoop() {
        loading = if (loading == dots.size - 1) 0 else loading + 1
        dots.forEachIndexed { index, view -> view.alpha = if (index == loading) 1f else 0.5f }
    }

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val runnable by lazy {
        Runnable {
            onLoop()
            handler.postDelayed(this, 400)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        handler.post(runnable)

        message.observe(this) {
            when(it) {
                "Checking for permissions..." -> onPermissionsCheck()
                "Checking for update..." -> onPlayStoreUpdateCheck()
                else -> {
                    toast("Starting...")
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAfterTransition()
                }
            }
        }
    }

    private fun onPlayStoreUpdateCheck() {
        manager.registerListener(this)
        manager.appUpdateInfo?.addOnCompleteListener {
            if (it?.isSuccessful == true) {
                if (it.result?.updateAvailability() == UPDATE_AVAILABLE &&
                    it.result?.isUpdateTypeAllowed(FLEXIBLE) == true)
                    manager.startUpdateFlowForResult(it.result, FLEXIBLE, this, UPDATE_CHECK)
                else message.value = "Starting..."
            } else message.value = "Starting..."
        }
    }

    private fun onPermissionsCheck() {
        if (isAvailable(this, Permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            message.value = "Checking for update..."
        } else ActivityCompat.requestPermissions(
            this,
            arrayOf(Permission.INTERNET),
            PERMISSION_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_CHECK) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    toast("Update cancelled")
                    message.value = "Starting..."
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    toast("Update failed")
                    message.value = "Starting..."
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (requestCode == PERMISSION_REQUEST)
            message.value = "Checking for update..."
    }

    override fun onStateUpdate(state: InstallState?) = when (state?.installStatus()) {
        InstallStatus.DOWNLOADED -> {
            AlertDialog.Builder(this).apply {
                title = "Update App"
                setMessage("Restart app to complete the update process")
                setCancelable(false)
                setPositiveButton("Restart") { dialog: DialogInterface, _ ->
                    manager.unregisterListener(this@ScreenA)
                    manager.completeUpdate()
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                    message.value = "Starting..."
                }
            }.create().show()
        }
        else -> Unit
    }
}