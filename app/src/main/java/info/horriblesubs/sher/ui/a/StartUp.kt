package info.horriblesubs.sher.ui.a

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import info.horriblesubs.sher.R
import info.horriblesubs.sher.databinding.AActivityBinding
import info.horriblesubs.sher.ui.PERMISSION_REQUEST
import info.horriblesubs.sher.ui.UPDATE_CHECK
import info.horriblesubs.sher.ui.b.MainActivity
import info.horriblesubs.sher.ui.toast
import info.horriblesubs.sher.ui.viewBindings
import android.Manifest.permission as Permission
import androidx.core.content.ContextCompat.checkSelfPermission as isAvailable

class StartUp: AppCompatActivity(), InstallStateUpdatedListener {

    private val manager by lazy { AppUpdateManagerFactory.create(this) }

    private val texts by lazy {
        listOf("Checking permissions...", "Checking for update...", "Starting...")
    }

    private val startUpChecks = MutableLiveData<Int>().apply { value = 0 }
    private var checks: Int
        get() = startUpChecks.value ?: 0
        set(value) { startUpChecks.value = value }

    init { checks = 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings<AActivityBinding>(R.layout.a_activity) {
            i = startUpChecks
            strings = texts
        }.root)

        startUpChecks.observe(this) {
            when(it) {
                0 -> onPermissionsCheck()
                1 -> onPlayStoreUpdateCheck()
                2, null -> {
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
                if (it.result?.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.result?.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) == true)
                    manager.startUpdateFlowForResult(it.result, AppUpdateType.FLEXIBLE, this,
                        UPDATE_CHECK)
                else
                    checks++
            } else
                checks++
        }
    }

    private fun onPermissionsCheck() {
        if (isAvailable(this, Permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            checks++
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
                    checks++
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    toast("Update failed")
                    checks++
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (requestCode == PERMISSION_REQUEST)
            checks++
    }

    override fun onStateUpdate(state: InstallState?) = when (state?.installStatus()) {
        InstallStatus.DOWNLOADED -> {
            AlertDialog.Builder(this).apply {
                title = "Update App"
                setMessage("Restart app to complete the update process")
                setCancelable(false)
                setPositiveButton("Restart") { dialog: DialogInterface, _ ->
                    manager.unregisterListener(this@StartUp)
                    manager.completeUpdate()
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                    checks++
                }
            }.create().show()
        }
        else -> Unit
    }
}