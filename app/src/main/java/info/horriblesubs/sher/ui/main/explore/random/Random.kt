package info.horriblesubs.sher.ui.main.explore.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.inflate
import info.horriblesubs.sher.dialog.RandomDialog

class Random : Fragment(), View.OnClickListener, Observer<ItemShow?> {

    private var dialog: RandomDialog? = null
    private var vm: RandomVM? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View? {
        return group?.inflate(R.layout._b_fragment_1_b, inflater)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        view.findViewById<View>(R.id.button).setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(RandomVM::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm?.result?.observe(viewLifecycleOwner, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.stop()
        destroy()
    }

    override fun onClick(v: View?) {
        vm?.refresh()
    }

    override fun onChanged(t: ItemShow?) {
        destroy()
        if (t != null)
            dialog = context?.let{RandomDialog(it, t)}
        dialog?.show()
    }

    private fun destroy() {
        dialog?.dismiss()
        dialog = null
    }
}