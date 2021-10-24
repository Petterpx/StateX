package com.petterp.statex.view

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.petterp.statex.StateX
import com.petterp.statex.StateX.defaultClickTime

/**
 * StateView扩展
 * @author petterp
 */
internal typealias stateBlock = View.(Any?) -> Unit

internal val viewConfig by lazy(LazyThreadSafetyMode.PUBLICATION) {
    StateViewConfig()
}

fun StateX.viewConfig(config: StateViewConfig.() -> Unit) {
    viewConfig.apply(config)
}

/**
 *  @author liangjingkanji
 *
 *  copy [https://github.com/liangjingkanji/StateLayout/blob/master/statelayout/src/main/java/com/drake/statelayout/StateLayout.kt]
 * */
class StateViewConfig {
    internal var retryIds: IntArray? = null
    internal var onEmpty: stateBlock? = null
    internal var onError: stateBlock? = null
    internal var onLoading: stateBlock? = null
    internal var onContent: stateBlock? = null
    var errorLayout = View.NO_ID
    var emptyLayout = View.NO_ID
    var loadingLayout = View.NO_ID

    fun onEmpty(block: stateBlock) {
        onEmpty = block
    }

    fun onError(block: stateBlock) {
        onError = block
    }

    fun onLoading(block: stateBlock) {
        onLoading = block
    }

    fun onContent(block: stateBlock) {
        onContent = block
    }

    fun setRetryIds(@IdRes vararg ids: Int) {
        retryIds = ids
    }
}

internal fun View.clickOne(time: Long = defaultClickTime, runnable: () -> Unit) {
    setOnClickListener {
        it?.isEnabled = false
        runnable()
        postDelayed({
            it?.isEnabled = true
        }, time)
    }
}

/**
 * @author liangjingkanji
 *
 * 创建一个缺省页来包裹Activity
 */
fun Activity.state(): StateView {
    val view = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    return view.state()
}

/**
 * @author liangjingkanji
 *
 * 创建一个缺省页来包裹Fragment
 */
fun Fragment.state(): StateView {
    val stateLayout = view!!.state()
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun removeState() {
            val parent = stateLayout.parent as ViewGroup
            parent.removeView(stateLayout)
            lifecycle.removeObserver(this)
        }
    })
    return stateLayout
}

/**
 * @author liangjingkanji
 *
 * 创建一个缺省页来包裹视图
 */
fun View.state(): StateView {
    val parent = parent as ViewGroup
    if (parent is ViewPager || parent is RecyclerView) {
        throw UnsupportedOperationException("You should using StateLayout wrap [ $this ] in layout when parent is ViewPager or RecyclerView")
    }
    val stateLayout = StateView(context)
    stateLayout.id = id
    val index = parent.indexOfChild(this)
    val layoutParams = layoutParams
    parent.removeView(this)
    parent.addView(stateLayout, index, layoutParams)
    when (this) {
        is ConstraintLayout -> {
            val contentViewLayoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            stateLayout.addView(this, contentViewLayoutParams)
        }
        else -> {
            stateLayout.addView(this)
        }
    }

    stateLayout.setContentView(this)
    return stateLayout
}
