package info.horriblesubs.sher.ui.show.detail.a

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.model.Time
import info.horriblesubs.sher.common.MenuHandler
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.common.load
import info.horriblesubs.sher.db.DataAccess
import info.horriblesubs.sher.ui.show.Show
import info.horriblesubs.sher.ui.show.ShowVM
import java.time.format.TextStyle
import java.util.*

class A : Fragment(), OnClickListener, PopupMenu.OnMenuItemClickListener {
    private var button2: AppCompatCheckedTextView? = null
    private var imageView: AppCompatImageView? = null
    private var textView1: AppCompatTextView? = null
    private var textView2: AppCompatTextView? = null
    private var textView3: AppCompatTextView? = null
    private var textView4: AppCompatTextView? = null
    private var show: ItemShow? = null
    private lateinit var model: ShowVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(activity as AppCompatActivity).get(ShowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_1_a, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MenuHandler(this, view, R.menu.menu_e, R.id.button3)
        view.findViewById<View>(R.id.button1).setOnClickListener(this)
        imageView = view.findViewById(R.id.imageView)
        textView4 = view.findViewById(R.id.textView4)
        textView3 = view.findViewById(R.id.textView3)
        textView2 = view.findViewById(R.id.textView2)
        textView1 = view.findViewById(R.id.textView1)
        button2 = view.findViewById(R.id.button2)
        button2?.setOnClickListener(this)
        model.result.observe(viewLifecycleOwner, Observer{observeShow(it)})
        model.time.observe(viewLifecycleOwner, Observer{textView4?.text = it})
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.report -> Toast.makeText(context, "Report Error", Toast.LENGTH_SHORT).show()
            R.id.refresh -> model.refresh(true)
        }
        return true
    }

    private fun observeShow(t: ItemShow?) {
        textView3?.text = if(t?.ongoing == true) "Currently Airing" else "Airing Completed"
        button2?.setText(if (DataAccess.bookmarked(t?.sid)) R.string.bookmarked else R.string.bookmark)
        button2?.isChecked = DataAccess.bookmarked(t?.sid)
        textView1?.text = t?.title.fromHtml()
        textView2?.text = t?.schedule.text()
        textView4?.text = t?.timePassed()
        imageView?.load(t?.image)
        show = t
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button1)
            (activity as Show?)?.viewReleases()
        if (v.id == R.id.button2) {
            DataAccess.autoBookmark(show)
            if (DataAccess.bookmarked(show?.sid)) {
                button2?.setText(R.string.bookmarked)
                button2?.isChecked = true
            } else {
                button2?.setText(R.string.bookmark)
                button2?.isChecked = false
            }
        }
    }
}

fun Time?.text(): String {
    if (this == null) return "Not Scheduled"
    return this.day().getDisplayName(TextStyle.FULL, Locale.US) + "'s at " + this.time()
}
