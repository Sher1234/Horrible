package info.horriblesubs.sher.libs.toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.gone
import info.horriblesubs.sher.ui.visible
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate", "Unused")
class Toolbar: FrameLayout, View.OnClickListener, Animation.AnimationListener,
    PopupMenu.OnMenuItemClickListener, OnFocusChangeListener, OnEditorActionListener {

    val runCustomizeMenu: Unit get() = { customizeMenu(menu?.menu) }()

    var customizeMenu: (menu: Menu?) -> Unit = {}

    private val appBarRootLayout: MaterialCardView
    private val menuButton: MaterialButton
    private val menuItem: MaterialButton
    private var menu: PopupMenu? = null

    private val searchBoxRoot: LinearLayoutCompat
    private val navigationButton: MaterialButton

    private val placeholderImageView: AppCompatImageView
    private val placeholderTextView: MaterialTextView
    private val searchBox: TextInputEditText

    var onToolbarActionListener: OnToolbarActionListener? = null

    @DrawableRes
    var navigationIconRes = -1
        set(value) {
            navigationButton.icon = if (value != -1) {
                navigationButton.visible
                ContextCompat.getDrawable(context, value)
            } else {
                navigationButton.gone
                null
            }
            field = value
        }

    fun onNavigationClickListener(listener: OnClickListener?) {
        navigationButton.setOnClickListener(listener)
    }

    fun setNavigationButton(@DrawableRes iconRes: Int, listener: OnClickListener) {
        onNavigationClickListener(listener)
        navigationIconRes = iconRes
    }

    @DrawableRes
    var placeholderImage: Int = -1
        set(value) {
            placeholderImageView.setImageDrawable(if (value != -1) {
                placeholderTextView.gone
                placeholderImageView.visible
                ContextCompat.getDrawable(context, value)
            } else {
                placeholderTextView.visible
                placeholderImageView.gone
                null
            })
            field = value
        }

    @JvmOverloads
    constructor(cxt: Context, attrs: AttributeSet? = null): super(cxt, attrs) {
        init(attrs)
    }

    @JvmOverloads
    constructor(cxt: Context, attrs: AttributeSet? = null, defStAt: Int): super(cxt, attrs, defStAt) {
        init(attrs)
    }

    init {
        View.inflate(context, R.layout._lib_toolbar, this)

        placeholderImageView = findViewById(R.id.placeholderImage)
        placeholderTextView = findViewById(R.id.placeholderText)
        navigationButton = findViewById(R.id.navigationButton)
        appBarRootLayout = findViewById(R.id.appBarRootLayout)
        searchBoxRoot = findViewById(R.id.searchBoxRoot)
        menuButton = findViewById(R.id.menuButton2)
        menuItem = findViewById(R.id.menuButton1)
        searchBox = findViewById(R.id.searchBox)

        findViewById<View>(R.id.searchClearButton)?.setOnClickListener(this)
        findViewById<View>(R.id.searchBackButton)?.setOnClickListener(this)
        searchBox?.setOnEditorActionListener(this)
        searchBox?.onFocusChangeListener = this
        setOnClickListener(this)
    }

    private fun init(attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.Toolbar)

        cornerRadius = array.getDimensionPixelSize(R.styleable.Toolbar_cornerRadius, -1)
        strokeWidth = array.getDimensionPixelSize(R.styleable.Toolbar_strokeWidth, -1)
        isSearchEnabled = array.getBoolean(R.styleable.Toolbar_enabled, true)
        strokeColor = array.getColor(R.styleable.Toolbar_strokeColor, -1)

        @SuppressLint("PrivateResource")
        navigationIconRes = array.getResourceId(R.styleable.Toolbar_navigationIcon, -1)
        @SuppressLint("PrivateResource")
        menuResId = array.getResourceId(R.styleable.Toolbar_menu, -1)

        searchTextColor = array.getColor(R.styleable.Toolbar_searchTextColor, -1)
        backgroundColorZ = array.getColor(R.styleable.Toolbar_backgroundColor, -1)
        query = array.getString(R.styleable.Toolbar_query)

        placeholderTextColor = array.getColor(R.styleable.Toolbar_placeholderTextColor, -1)
        placeholderImage = array.getResourceId(R.styleable.Toolbar_placeholderImage, -1)
        placeholderText = array.getString(R.styleable.Toolbar_placeholderText)

        hintTextColor = array.getColor(R.styleable.Toolbar_hintColor, -1)
        hint = array.getString(R.styleable.Toolbar_hint)

        searchBox.addTextChangedListener {
            onToolbarActionListener?.onQueryChanged(it?.toString())
        }
        onCloseSearchBar
        array.recycle()
    }

    var isSearchEnabled = true
        set(value) {
            setOnClickListener(if (value) this else null)
            isClickable = value
            field = value
        }

    private val onCloseSearchBar: Unit get() {
        placeholderTextColor = placeholderTextColor
        navigationIconRes = navigationIconRes
        backgroundColorZ = backgroundColorZ
        placeholderImage = placeholderImage
        placeholderText = placeholderText
        searchTextColor = searchTextColor
        hintTextColor = hintTextColor
        cornerRadius = cornerRadius
        strokeWidth = strokeWidth
        strokeColor = strokeColor
        menuResId = menuResId
        query = query
        hint = hint
    }

    @MenuRes
    private var menuResId = -1
        set(value) {
            if (value != -1) {
                val popupMenu = PopupMenu(context, menuButton).apply {
                    setOnMenuItemClickListener(this@Toolbar)
                    gravity = Gravity.END
                    inflate(value)
                }
                popupMenu.menu.getFirstVisible?.apply {
                    menuButton.setOnLongClickListener {
                        Toast.makeText(context, "Menu", Toast.LENGTH_SHORT).show()
                        true
                    }
                    menuItem.setOnLongClickListener {
                        Toast.makeText(context, this.title, Toast.LENGTH_SHORT).show()
                        true
                    }
                    menuItem.setOnClickListener {
                        onMenuItemClick(this)
                    }
                    popupMenu.menu.findItem(this.itemId)?.apply {
                        this.isVisible = false
                        this.isEnabled = false
                    }
                    menuItem.icon = this.icon
                    menuItem.visible
                }
                if (popupMenu.menu.hasRemainingMenu) {
                    menuButton.setIconResource(R.drawable.ic_menu)
                    menuButton.setOnClickListener(this)
                    menuButton.visible
                } else menuButton.gone
                customizeMenu(popupMenu.menu)
                this.menu = popupMenu
            } else {
                menuItem.setOnLongClickListener(null)
                menuButton.setOnClickListener(null)
                menuItem.setOnClickListener(null)
                menuItem.icon = null
                menuButton.gone
                menuItem.gone
                menu = null
            }
            field = value
        }

    private val Menu.getFirstVisible: MenuItem? get() {
        val s = arrayListOf<MenuItem>()
        if (hasVisibleItems()) {
            forEach {
                if (it.isVisible && it.icon != null)
                    s.add(it)
            }
            s.sortWith(Comparator { o1, o2 ->
                val order1 = o1?.order ?: 0
                val order2 = o2?.order ?: 0
                when {
                    order1 < order2 -> -1
                    order1 > order2 -> 1
                    else -> 0
                }
            })
            return if (s.isEmpty()) null else s[0]
        } else return null
    }

    private val Menu.hasRemainingMenu: Boolean get() {
        val s = arrayListOf<MenuItem>()
        return if (hasVisibleItems()) {
            forEach {
                if (it.isVisible)
                    s.add(it)
            }
            s.sortWith(Comparator { o1, o2 ->
                val order1 = o1?.order ?: 0
                val order2 = o2?.order ?: 0
                when {
                    order1 < order2 -> -1
                    order1 > order2 -> 1
                    else -> 0
                }
            })
            s.isNotEmpty()
        } else false
    }

    fun inflateMenu(@MenuRes menuRes: Int) {
        menuResId = menuRes
    }

    fun removeMenu() {
        menuResId = -1
    }

    var cornerRadius: Int
        get() = appBarRootLayout.radius.roundToInt()
        set(value) {
            if (value != -1)
                appBarRootLayout.radius = value.toFloat()
        }

    @ColorInt
    var backgroundColorZ: Int = -1
        set(value) {
            if (value != -1) {
                appBarRootLayout.setCardBackgroundColor(value)
                field = value
            }
        }

    @ColorInt
    var searchTextColor: Int = -1
        set(value) {
            if (value != -1) {
                searchBox.setTextColor(value)
                field = value
            }
        }

    @ColorInt
    var hintTextColor: Int = -1
        set(value) {
            if (value != -1) {
                searchBox.setHintTextColor(value)
                field = value
            }
        }

    @ColorInt
    var placeholderTextColor: Int = -1
        set(value) {
            if (value != -1) {
                placeholderTextView.setTextColor(value)
                field = value
            }
        }

    var hint: CharSequence?
        set(value) { searchBox.hint = value }
        get() = searchBox.hint

    var placeholderText: CharSequence?
        set(value) { placeholderTextView.text = value }
        get() = placeholderTextView.text

    fun removeOnToolbarActionListener() {
        onToolbarActionListener = null
    }

    var isSearchOpened = false
        private set(value) {
            if(value) {
                if (field) {
                    onToolbarActionListener?.onSearchStateChanged(true)
                    searchBox.requestFocus()
                    return
                }
                field = value
                val leftIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_left)
                val leftOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_left)
                leftIn.setAnimationListener(this)
                if (placeholderImage != -1)
                    placeholderImageView.startAnimation(leftOut)
                if (!placeholderText.isNullOrBlank())
                    placeholderTextView.startAnimation(leftOut)
                if (navigationIconRes != -1)
                    navigationButton.startAnimation(leftOut)
                if (menuResId != -1)
                    menuButton.startAnimation(leftOut)
                if (menuItem.icon != null)
                    menuItem.startAnimation(leftOut)
                searchBoxRoot.startAnimation(leftIn)
                if (isListenerExists)
                    onToolbarActionListener?.onSearchStateChanged(true)
            } else {
                field = value
                val rightOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_right)
                val rightIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_right)
                rightOut.setAnimationListener(this)
                if (placeholderImage != -1)
                    placeholderImageView.startAnimation(rightIn)
                if (!placeholderText.isNullOrBlank())
                    placeholderTextView.startAnimation(rightIn)
                if (navigationIconRes != -1)
                    navigationButton.startAnimation(rightIn)
                if (menuResId != -1)
                    menuButton.startAnimation(rightIn)
                if (menuItem.icon != null)
                    menuItem.startAnimation(rightIn)
                searchBoxRoot.startAnimation(rightOut)
                if (isListenerExists) onToolbarActionListener?.onSearchStateChanged(false)
            }
        }

    val closeSearch: Unit get() {
        isSearchOpened = false
    }

    val openSearch: Unit get() {
        isSearchOpened = true
    }

    var strokeWidth: Int
        get() = appBarRootLayout.strokeWidth
        set(value) {
            if (value != -1)
                appBarRootLayout.strokeWidth = value
        }

    var strokeColor: Int
        get() = appBarRootLayout.strokeColorStateList?.defaultColor ?: -1
        set(value) {
            if (value != -1)
                appBarRootLayout.strokeColor = value
        }

    var query: String?
        get() = searchBox.text?.toString()
        set(text) {
            searchBox.setText(text)
        }

    private val isListenerExists: Boolean get() = onToolbarActionListener != null

    override fun onClick(v: View?) {
        when (v?.id) {
            id -> {
                if (!isSearchOpened)
                    openSearch
            }
            R.id.searchBackButton -> {
                closeSearch
            }
            R.id.searchClearButton -> {
                query = ""
            }
            R.id.menuButton2 -> {
                menu?.show()
            }
        }
    }

    override fun onAnimationRepeat(animation: Animation) = Unit
    override fun onAnimationStart(animation: Animation) = Unit
    override fun onAnimationEnd(animation: Animation) {
        if (isSearchOpened) {
            menuItem.gone
            menuButton.gone
            navigationButton.gone
            placeholderTextView.gone
            placeholderImageView.gone
            searchBoxRoot.visible
            searchBox.requestFocus()
        } else {
            searchBoxRoot.gone
            onCloseSearchBar
            query = ""
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (isListenerExists) onToolbarActionListener?.onQueryChanged(query)
        query?.let { onToolbarActionListener?.onQueryChanged(it) }
        return true
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (hasFocus) imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        else imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun onMenuItemClick(item: MenuItem?) = item?.let {
        onToolbarActionListener?.onMenuItemClickListener(it)
        true
    } ?: false

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && isSearchOpened) {
            closeSearch
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    interface OnToolbarActionListener {
        fun onMenuItemClickListener(item: MenuItem) = Unit
        fun onSearchStateChanged(enabled: Boolean) = Unit
        fun onQueryChanged(text: String?) = Unit
    }
}
