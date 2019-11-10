package info.horriblesubs.sher.ui.show.detail.a

import android.content.Intent
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
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.model.Time
import info.horriblesubs.sher.common.*
import info.horriblesubs.sher.db.DataAccess
import info.horriblesubs.sher.dialog.NetworkErrorDialog
import info.horriblesubs.sher.ui.show.Show
import info.horriblesubs.sher.ui.show.ShowVM
import java.time.format.TextStyle
import java.util.*

class A : Fragment(), OnClickListener, PopupMenu.OnMenuItemClickListener {
    private var button2a: AppCompatCheckedTextView? = null
    private var errorDialog: NetworkErrorDialog? = null
    private var button2b: AppCompatImageButton? = null
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

    override fun onDestroy() {
        errorDialog?.dismiss()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MenuHandler(this, view, R.menu.menu_e, Constants.orientation(R.id.button3a, R.id.button3b))
        view.findViewById<View>(Constants.orientation(R.id.button1a, R.id.button1b)).setOnClickListener(this)
        errorDialog = context?.let{NetworkErrorDialog(it)}
        imageView = view.findViewById(R.id.imageView)
        textView4 = view.findViewById(R.id.textView4)
        textView3 = view.findViewById(R.id.textView3)
        textView2 = view.findViewById(R.id.textView2)
        textView1 = view.findViewById(R.id.textView1)
        button2a = view.findViewById(R.id.button2a)
        button2b = view.findViewById(R.id.button2b)
        button2a?.setOnClickListener(this)
        button2b?.setOnClickListener(this)
        model.result.observe(viewLifecycleOwner, Observer{observeShow(it)})
        model.time.observe(viewLifecycleOwner, Observer{textView4?.text = it})
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.refresh -> model.refresh(true)
            R.id.share -> {
                if(show?.title == null || show?.link == null)
                    Toast.makeText(context, "Invalid info", Toast.LENGTH_SHORT).show()
                else {
                    val link = if(show?.link?.contains("shows") == true) show?.link
                    else "https://horriblesubs.info/shows/${show?.link}"
                    val text = "Title: ${show?.title}\nLink: $link"
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        }
        return true
    }

    private fun observeShow(t: ItemShow?) {
        if (t?.title == null || t.link == null) {
            if (model.link2.isNullOrEmpty()) Toast.makeText(context, "Undefined URL", Toast.LENGTH_SHORT).show()
            else errorDialog?.show("https://horriblesubs.info/show/${model.link2}")
        }
        textView3?.text = if(t?.ongoing == true) "Currently Airing" else "Airing Completed"
        if (Constants.orientation(p = true, l = false)) {
            button2a?.setText(if (DataAccess.bookmarked(t?.sid)) R.string.bookmarked else R.string.bookmark)
            button2a?.isChecked = DataAccess.bookmarked(t?.sid)
        } else {
            val icon = if (DataAccess.bookmarked(t?.sid)) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
            button2b?.setImageResource(icon)
        }
        textView1?.text = t?.title.fromHtml()
        textView2?.text = t?.schedule.text()
        textView4?.text = t?.timePassed()
        imageView?.load(t?.image)
        show = t
    }

    override fun onClick(v: View) {
        if (v.id == Constants.orientation(R.id.button1a, R.id.button1b))
            (activity as Show?)?.viewReleases()
        if (v.id == R.id.button2a || v.id == R.id.button2b) {
            DataAccess.autoBookmark(show)
            if (Constants.orientation(p = true, l = false)) {
                button2a?.setText(if (DataAccess.bookmarked(show?.sid)) R.string.bookmarked else R.string.bookmark)
                button2a?.isChecked = DataAccess.bookmarked(show?.sid)
            } else {
                val icon = if (DataAccess.bookmarked(show?.sid)) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
                button2b?.setImageResource(icon)
            }
        }
    }
}

fun Time?.text(): String {
    if (this == null) return "Not Scheduled"
    return this.day().getDisplayName(TextStyle.FULL, Locale.US) + "'s at " + this.time()
}
