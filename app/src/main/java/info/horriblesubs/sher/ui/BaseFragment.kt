package info.horriblesubs.sher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import info.horriblesubs.sher.ui._extras.listeners.OnBackPressedListener

abstract class BaseFragment: Fragment(), OnBackPressedListener {
    abstract val layoutId: Int
    abstract val name: String

    override fun <T : AppCompatActivity> onBackPressed(t: T) = true
    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(layoutId, group, false)
    }
}
