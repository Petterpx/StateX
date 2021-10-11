package com.petterp.state.compose

import com.petterp.state.StateEnum

/**
 * Compose 状态控制器实例
 * @author petterp
 */
class StateComposeImpl internal constructor(override var status: StateEnum = StateEnum.CONTENT) :
    IStateCompose {
    private var onEmpty: ((Any?) -> Unit)? = null
    private var onContent: ((Any?) -> Unit)? = null
    private var onError: ((Any?) -> Unit)? = null
    private var onLoading: ((Any?) -> Unit)? = null
    private var onRefresh: ((Any?) -> Unit)? = null

    override fun onError(block: stateBlock) {
        this.onError = block
    }

    override fun onContent(block: stateBlock) {
        this.onContent = block
    }

    override fun onLoading(block: stateBlock) {
        this.onLoading = block
    }

    override fun onRefresh(block: stateBlock) {
        this.onRefresh = block
    }

    override fun onEmpty(block: stateBlock) {
        this.onEmpty = block
    }

    override fun showError(tag: Any?): IStateCompose {
        if (status == StateEnum.ERROR)
            return this
        onError?.invoke(tag)
        return copy(status = StateEnum.ERROR)
    }

    override fun showContent(tag: Any?): IStateCompose {
        if (status == StateEnum.CONTENT)
            return this
        onContent?.invoke(tag)
        return copy(status = StateEnum.CONTENT)
    }

    override fun showEmpty(tag: Any?): IStateCompose {
        if (status == StateEnum.EMPTY)
            return this
        onEmpty?.invoke(tag)
        return copy(status = StateEnum.EMPTY)
    }

    override fun showLoading(
        tag: Any?,
        silent: Boolean,
        refresh: Boolean
    ): IStateCompose {
        if (status == StateEnum.LOADING)
            return this
        if (silent && refresh) {
            onRefresh?.invoke(tag)
            return this
        }
        onLoading?.invoke(tag)
        return copy(status = StateEnum.LOADING)
    }

    private fun copy(status: StateEnum): IStateCompose =
        StateComposeImpl(status = status).also {
            it.onContent = onContent
            it.onEmpty = onEmpty
            it.onLoading = onLoading
            it.onRefresh = onRefresh
            it.onError = onError
        }
}
