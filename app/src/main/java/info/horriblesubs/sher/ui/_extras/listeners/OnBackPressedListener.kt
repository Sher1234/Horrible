package info.horriblesubs.sher.ui._extras.listeners

import androidx.appcompat.app.AppCompatActivity

interface OnBackPressedListener {
    fun <T: AppCompatActivity> onBackPressed(t: T): Boolean
}