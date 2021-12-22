package com.petterp.statex.view

import android.view.View
import androidx.annotation.IdRes

object StateViewConfig {
    internal var retryIds: IntArray? = null
    internal var onEmpty: stateBlock? = null
    internal var onError: stateBlock? = null
    internal var onLoading: stateBlock? = null
    internal var onContent: stateBlock? = null
    var errorLayout = View.NO_ID
    var emptyLayout = View.NO_ID
    var loadingLayout = View.NO_ID

    /** 默认点击防抖时间 */
    var defaultClickTime = 600L

    /** 空数据重试开关 */
    var enableNullRetry = true

    /** 异常重试开关 */
    var enableErrorRetry = true

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
