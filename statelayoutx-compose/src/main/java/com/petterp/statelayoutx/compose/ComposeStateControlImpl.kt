package com.petterp.statelayoutx.compose

import com.petterp.statelayoutx.StateEnum

/**
 * 状态控制器实例
 * @author petterp
 */
class ComposeStateControlImpl private constructor(override val status: StateEnum = StateEnum.CONTENT) :
    IStateControl {
    private var onEmpty: ((Any?) -> Unit)? = null
    private var onContent: ((Any?) -> Unit)? = null
    private var onError: ((Any?) -> Unit)? = null
    private var onLoading: ((Any?) -> Unit)? = null
    private var onRefresh: ((Any?) -> Unit)? = null

    override fun onError(block: onStateBack) {
        this.onError = block
    }

    override fun onContent(block: onStateBack) {
        this.onContent = block
    }

    override fun onLoading(block: onStateBack) {
        this.onLoading = block
    }

    override fun onRefresh(block: onStateBack) {
        this.onRefresh = block
    }

    override fun onEmpty(block: onStateBack) {
        this.onEmpty = block
    }

    override fun showError(tag: Any?): IStateControl {
        if (status == StateEnum.ERROR)
            return this
        onError?.invoke(tag)
        return copy(status = StateEnum.ERROR)
    }

    override fun showContent(tag: Any?): IStateControl {
        if (status == StateEnum.CONTENT)
            return this
        onContent?.invoke(tag)
        return copy(status = StateEnum.CONTENT)
    }

    override fun showEmpty(tag: Any?): IStateControl {
        if (status == StateEnum.EMPTY)
            return this
        onEmpty?.invoke(tag)
        return copy(status = StateEnum.EMPTY)
    }

    override fun showLoading(
        tag: Any?,
        silent: Boolean,
        refresh: Boolean
    ): IStateControl {
        if (status == StateEnum.LOADING)
            return this
        if (silent && refresh) {
            onRefresh?.invoke(tag)
            return this
        }
        onLoading?.invoke(tag)
        return copy(status = StateEnum.LOADING)
    }

    private fun copy(status: StateEnum): IStateControl =
        ComposeStateControlImpl(status = status).also {
            it.onContent = onContent
            it.onEmpty = onEmpty
            it.onLoading = onLoading
            it.onRefresh = onRefresh
            it.onError = onError
        }

    companion object {
        fun init(status: StateEnum = StateEnum.CONTENT): IStateControl =
            ComposeStateControlImpl(status)
    }
}
