package com.petterp.statex.view

import android.view.View
import androidx.annotation.IdRes
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
