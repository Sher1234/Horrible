package info.horriblesubs.sher.ui.show.detail.b

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.ui.show.ShowVM

class B : Fragment(), Observer<ItemShow?> {
    private var textView: AppCompatTextView? = null
    private var model: ShowVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(activity as AppCompatActivity).get(ShowVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._c_fragment_1_b, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.textView)
        model?.result?.observe(viewLifecycleOwner, this)
    }

    override fun onChanged(t: ItemShow?) {
        textView?.text = t?.body.fromHtml()
    }
}