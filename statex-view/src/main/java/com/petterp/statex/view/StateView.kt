package com.petterp.statex.view

import android.content.Context
import android.content.res.Resources
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.collection.ArrayMap
import com.petterp.statex.StateEnum
import com.petterp.statex.StateX.defaultClickTime

/**
 * 状态View
 * @author liangjingkanji
 *
 * copy [https://github.com/liangjingkanji/StateLayout/blob/master/statelayout/src/main/java/com/drake/statelayout/StateLayout.kt]
 * 部分修改
 */
class StateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IStateView {

    private val views = ArrayMap<StateEnum, View>()
    private var refresh = true
    private var stateChanged = false
    private var trigger = true

    private var clickTime: Long = defaultClickTime
    private var retryIds: IntArray? = null
        get() = field ?: viewConfig.retryIds
    private var onEmpty: stateBlock? = null
        get() = field ?: viewConfig.onEmpty
    private var onError: stateBlock? = null
        get() = field ?: viewConfig.onError
    private var onContent: stateBlock? = null
        get() = field ?: viewConfig.onContent
    private var onLoading: stateBlock? = null
        get() = field ?: viewConfig.onLoading
    private var onRefresh: (StateView.(tag: Any?) -> Unit)? = null

    private var _state: StateEnum = StateEnum.CONTENT

    override val state: StateEnum
        get() = _state

    /** 当前缺省页是否加载成功过, 即是否执行过[showContent] */
    var loaded = false

    /** 错误页面布局 */
    @LayoutRes
    var errorLayout: Int = NO_ID
        get() = if (field == NO_ID) viewConfig.errorLayout else field
        set(value) {
            if (field != value) {
                removeStatus(StateEnum.ERROR)
                field = value
            }
        }

    /** 空页面布局 */
    @LayoutRes
    var emptyLayout: Int = NO_ID
        get() = if (field == NO_ID) viewConfig.emptyLayout else field
        set(value) {
            if (field != value) {
                removeStatus(StateEnum.EMPTY)
                field = value
            }
        }

    /** 加载中页面布局 */
    @LayoutRes
    var loadingLayout: Int = NO_ID
        get() = if (field == NO_ID) viewConfig.loadingLayout else field
        set(value) {
            if (field != value) {
                removeStatus(StateEnum.LOADING)
                field = value
            }
        }

    // </editor-fold>

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StateView)
        try {
            emptyLayout = attributes.getResourceId(R.styleable.StateView_empty_layout, NO_ID)
            errorLayout = attributes.getResourceId(R.styleable.StateView_error_layout, NO_ID)
            loadingLayout = attributes.getResourceId(R.styleable.StateView_loading_layout, NO_ID)
        } finally {
            attributes.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1 || childCount == 0) {
            throw UnsupportedOperationException("StateLayout only have one child view")
        }
        if (views.size == 0) {
            val view = getChildAt(0)
            setContentView(view)
        }
    }

    // <editor-fold desc="监听缺省页">

    /**
     * 当空缺省页显示时回调
     * @see showEmpty
     * @see StateConfig.onEmpty
     */
    override fun onEmpty(block: stateBlock) = apply {
        onEmpty = block
    }

    /**
     * 当错误缺省页显示时回调
     * @see showError
     * @see StateConfig.onError
     */
    override fun onError(block: stateBlock) = apply {
        onError = block
    }

    /**
     * 当加载中缺省页显示时回调
     * @see showLoading
     * @see StateConfig.onLoading
     */
    override fun onLoading(block: stateBlock) = apply {
        onLoading = block
    }

    /**
     * 当[showLoading]时会回调该函数参数, 一般将网络请求等异步操作放入其中
     */
    override fun onRefresh(block: StateView.(tag: Any?) -> Unit) = apply {
        onRefresh = block
    }

    /**
     * 当[showContent]时会回调该函数参数, 一般将网络请求等异步操作放入其中
     * @see showContent
     * @see StateConfig.onContent
     */
    override fun onContent(block: stateBlock) = apply {
        onContent = block
    }

    // </editor-fold>

    // <editor-fold desc="显示缺省页">

    /**
     * 有网则显示加载中, 无网络直接显示错误, 会触发[onLoading]的函数参数
     * @param tag 传递任意对象给[onLoading]函数
     * @param silent 仅执行[onRefresh], 不会显示加载中布局, 也不执行[onLoading]
     * @param refresh 是否回调[onRefresh]
     */
    override fun showLoading(tag: Any?, silent: Boolean, refresh: Boolean) {
        this.refresh = refresh
        if (silent && refresh) {
            onRefresh?.invoke(this, tag)
        }
        if (state == StateEnum.LOADING) {
            onLoading?.invoke(getStatusView(StateEnum.LOADING), tag)
        }
        show(StateEnum.LOADING, tag)
    }

    /**
     * 显示空页, 会触发[onEmpty]的函数参数
     * @param tag 传递任意对象给[onEmpty]函数
     */
    override fun showEmpty(tag: Any?) {
        show(StateEnum.EMPTY, tag)
    }

    /**
     * 显示错误页, 会触发[onError]的函数参数
     * @param tag 传递任意对象给[onError]函数
     */
    override fun showError(tag: Any?) {
        show(StateEnum.ERROR, tag)
    }

    /**
     * 显示内容布局, 表示成功缺省页
     * @param tag 传递任意对象给[onContent]函数
     */
    override fun showContent(tag: Any?) {
        if (trigger && stateChanged)
            show(StateEnum.CONTENT, tag)
    }

    // </editor-fold>

    /**
     * 为错误页/空页中的指定Id控件设置点击事件, 点击会触发[showLoading]
     * 默认点击500ms内防抖动
     */
    fun setRetryIds(@IdRes vararg ids: Int, clickTime: Long = defaultClickTime) = apply {
        retryIds = ids
        this.clickTime = clickTime
    }

    /**
     * 本函数为方便其他框架热插拔使用, 开发者一般情况不使用
     * 本函数调用两次之间显示缺省页只会有效执行一次
     */
    fun trigger(): Boolean {
        trigger = !trigger
        if (!trigger) stateChanged = false
        return trigger
    }

    /**
     * 显示视图
     */
    private fun show(state: StateEnum, tag: Any? = null) {
        if (trigger) stateChanged = true
        this._state = state
        runMain {
            try {
                val view = showState(state)
                when (state) {
                    StateEnum.EMPTY -> {
                        retryIds?.forEach {
                            view.findViewById<View>(it)?.clickOne(clickTime) {
                                showLoading()
                            }
                        }
                        onEmpty?.invoke(view, tag)
                    }
                    StateEnum.ERROR -> {
                        retryIds?.forEach {
                            view.findViewById<View>(it)?.clickOne(clickTime) {
                                showLoading()
                            }
                        }
                        onError?.invoke(view, tag)
                    }
                    StateEnum.LOADING -> {
                        onLoading?.invoke(view, tag)
                        if (refresh) onRefresh?.invoke(this, tag)
                    }
                    else -> {
                        loaded = true
                        onContent?.invoke(view, tag)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showState(state: StateEnum): View {
        val target = getStatusView(state)
        for (view in views.values) {
            if (target == view) {
                view.visibility = VISIBLE
            } else {
                view.visibility = GONE
            }
        }
        return target
    }

    /**
     * 删除指定的缺省页
     */
    private fun removeStatus(state: StateEnum?) {
        views.remove(state)?.let { removeView(it) }
    }

    /**
     * 返回缺省页视图对象
     */
    @Throws(NullPointerException::class)
    private fun getStatusView(state: StateEnum): View {
        views[state]?.let { return it }
        val layoutId = when (state) {
            StateEnum.EMPTY -> emptyLayout
            StateEnum.ERROR -> errorLayout
            StateEnum.LOADING -> loadingLayout
            else -> NO_ID
        }
        if (layoutId == NO_ID) {
            when (state) {
                StateEnum.ERROR -> throw Resources.NotFoundException("No StateLayout errorLayout is set")
                StateEnum.EMPTY -> throw Resources.NotFoundException("No StateLayout emptyLayout is set")
                StateEnum.LOADING -> throw Resources.NotFoundException("No StateLayout loadingLayout is set")
                else -> throw Resources.NotFoundException("No StateLayout contentView is set")
            }
        }
        val view = LayoutInflater.from(context).inflate(layoutId, this, false)
        addView(view)
        views[state] = view
        return view
    }

    /**
     * 标记视图为内容布局, 本函数为其他框架进行热插拔适配使用, 一般情况开发者不使用
     */
    fun setContentView(view: View) {
        views[StateEnum.CONTENT] = view
    }

    /**
     * 保证运行在主线程
     */
    private fun runMain(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block()
        } else {
            handler.post(block)
        }
    }
}
