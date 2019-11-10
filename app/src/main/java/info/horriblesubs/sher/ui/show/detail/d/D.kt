package info.horriblesubs.sher.ui.show.detail.d

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.ui.show.ShowVM
import info.horriblesubs.sher.ui.web.WebX

class D : Fragment(), OnClickListener, Observer<ItemShow?> {

    private val listener = OnClickListener{view ->
        Snackbar.make(view, "Invalid Show Details", Snackbar.LENGTH_SHORT).show()
    }
    private var button1: MaterialButton? = null
    private var button2: MaterialButton? = null
    private var button3: MaterialButton? = null
    private var button4: MaterialButton? = null
    private var show: ItemShow? = null
    private var model: ShowVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(activity as AppCompatActivity).get(ShowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_1_d, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button4 = view.findViewById(R.id.button4)
        button3 = view.findViewById(R.id.button3)
        button2 = view.findViewById(R.id.button2)
        button1 = view.findViewById(R.id.button1)
        button4?.setOnClickListener(listener)
        button3?.setOnClickListener(listener)
        button2?.setOnClickListener(listener)
        button1?.setOnClickListener(listener)
        model?.result?.observe(viewLifecycleOwner, this)
    }

    override fun onChanged(t: ItemShow?) {
        if (t != null) {
            button2?.text = getString(if(t.mal_id != null && t.mal_id.isNotEmpty()) R.string.view_in_mal else R.string.search_in_mal)
            button4?.setOnClickListener(this)
            button3?.setOnClickListener(this)
            button2?.setOnClickListener(this)
            button1?.setOnClickListener(this)
        } else {
            button4?.setOnClickListener(listener)
            button3?.setOnClickListener(listener)
            button2?.setOnClickListener(listener)
            button1?.setOnClickListener(listener)
        }
        show = t
    }

    override fun onClick(v: View) {
        val link: String?
        var intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        when (v.id) {
            R.id.button1 -> {
                if (show?.link == null) return
                intent = Intent(context, WebX::class.java)
                link = if(show?.link?.contains("shows") == true)show?.link
                else "https://horriblesubs.info/shows/${show?.link}"
                intent.putExtra("link", link)
                startActivity(intent)
            }
            R.id.button2 -> {
                if (show == null) return
                link = if (!show?.mal_id.isNullOrEmpty()) "https://myanimelist.net/anime/${show?.mal_id}"
                else "https://myanimelist.net/anime.php?q=${Uri.encode(show?.title)}"
                intent.data = Uri.parse(link)
                startActivity(intent)
            }
            R.id.button3 -> {
                if (show == null) return
                link = "https://nyaa.si/?f=0&c=0_0&q=${Uri.encode(show?.title)}"
                intent.data = Uri.parse(link)
                startActivity(intent)
            }
            R.id.button4 -> {
                if (show == null) return
                link = "https://animekaizoku.com//?s=${Uri.encode(show?.title)}"
                intent.data = Uri.parse(link)
                startActivity(intent)
            }
        }
    }
}