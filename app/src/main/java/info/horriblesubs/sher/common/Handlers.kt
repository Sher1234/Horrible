package info.horriblesubs.sher.common

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo.*
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.PopupMenu.OnMenuItemClickListener
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textfield.TextInputEditText
import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import java.util.*


@Suppress("unused")
class SearchMenuHandler (
    menuItemClickListener: OnMenuItemClickListener?,
    searchListener: SearchListener?,
    view: View?,
    menu: Int,
    isReleases: Boolean = false
) {
    val menuHandler = MenuHandler(menuItemClickListener, view, menu)
    init {
        SearchHandler(searchListener, view, isReleases)
    }
}

interface SearchListener {
    fun search(s: String?)
}

class MenuHandler (
    menuItemClickListener: OnMenuItemClickListener?,
    view: View?,
    menu: Int,
    buttonId: Int = R.id.button
) {
    private val themeContext = ContextThemeWrapper(App.instance, R.style.Theme_PopupMenu)
    private val button: View? = view?.findViewById(buttonId)
    internal val popupMenu: PopupMenu = PopupMenu(themeContext, button)
    init {
        button?.setOnClickListener{popupMenu.show()}
        popupMenu.menuInflater.inflate(menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(menuItemClickListener)
    }
}

class SearchHandler (
    private val searchListener: SearchListener?,
    view: View?,
    releases: Boolean = false
): TextWatcher {
    private val editText: TextInputEditText? = view?.findViewById(R.id.searchBar)
    private val logo: AppCompatImageView? = view?.findViewById(R.id.logo_)

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        searchListener?.search(editText?.text.toString())
    }

    override fun afterTextChanged(s: Editable?) {}

    init {
        if (!releases) view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = view.rootView.height
            val keypadHeight: Int = screenHeight - r.bottom
            if (keypadHeight <= screenHeight * 0.2)
                editText?.clearFocus()
        }
        editText?.inputType = if(releases) TYPE_CLASS_NUMBER else TYPE_TEXT_VARIATION_PERSON_NAME
        editText?.addTextChangedListener(this)
        editText?.ellipsize = TextUtils.TruncateAt.END
        editText?.onEditorAction(IME_ACTION_SEARCH)
        editText?.setOnEditorActionListener {v, i, _ ->
            if (i == IME_ACTION_SEARCH || i == IME_ACTION_DONE || i == IME_ACTION_GO || i == IME_ACTION_NEXT) {
                searchListener?.search(editText.text.toString())
                (App.instance.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?)
                    ?.hideSoftInputFromWindow(v?.windowToken, Random().nextInt(999))
            }
            true
        }
        editText?.setOnFocusChangeListener{_, focus ->
            logo?.visibility = if (!focus)
                if (!releases && editText.text.isNullOrEmpty()) View.VISIBLE else View.GONE else View.GONE
        }
        editText?.setSingleLine()
        editText?.maxLines = 1
        if (releases) {
            editText?.setCompoundDrawables(null, null, null, null)
            editText?.hint = "Search Releases"
            logo?.visibility = View.GONE
        }
    }
}

class ErrorHandler (private val listener: ErrorListener?, view: View?) {
    private val imageView: AppCompatImageView? = view?.findViewById(R.id.errorImage)
    private val textView: AppCompatTextView? = view?.findViewById(R.id.errorText)
    private val layout: View? = view?.findViewById(R.id.errorLayout)
    fun show(isError: Boolean = false) {
        imageView?.load(if (isError) "error.png" else "empty.png", true)
        textView?.visibility = if (isError) View.GONE else View.VISIBLE
        textView?.visibility = if (isError) View.GONE else View.VISIBLE
        layout?.visibility = View.VISIBLE
        listener?.errorVisible()
    }
    fun hide() {
        layout?.visibility = View.GONE
        listener?.errorHidden()
    }
    init {
        layout?.visibility = View.GONE
    }
}

interface ErrorListener {
    fun errorVisible()
    fun errorHidden()
}