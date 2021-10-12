package com.petterp.statex.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.petterp.statex.StateEnum

/**
 * Compose 状态控制器实例,对于外部,通过[IStateCompose]控制
 * @author petterp
 */
class StateCompose internal constructor() : IStateCompose {

    // 内部状态
    internal var internalState by mutableStateOf(StateEnum.CONTENT)

    override val state: StateEnum
        get() = internalState

    private var onEmpty: stateBlock? = composeConfig.onEmpty
    private var onContent: stateBlock? = composeConfig.onContent
    private var onError: stateBlock? = composeConfig.onError
    private var onLoading: stateBlock? = composeConfig.onLoading
    private var onRefresh: stateBlock? = null

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

    override fun showError(tag: Any?) {
        if (internalState == StateEnum.ERROR)
            return
        onError?.invoke(tag)
        internalState = StateEnum.ERROR
    }

    override fun showContent(tag: Any?) {
        onContent?.invoke(tag)
        if (internalState == StateEnum.CONTENT) return
        internalState = StateEnum.CONTENT
    }

    override fun showEmpty(tag: Any?) {
        onEmpty?.invoke(tag)
        if (internalState == StateEnum.EMPTY) return
        internalState = StateEnum.EMPTY
    }

    override fun showLoading(
        tag: Any?,
        silent: Boolean,
        refresh: Boolean
    ) {
        // 每次loading都会触发,如果同时silent与refresh满足,则触发refresh
        onLoading?.invoke(tag)
        if (silent && refresh) {
            onRefresh?.invoke(tag)
        }
        if (internalState == StateEnum.LOADING) return
        if (refresh) onRefresh?.invoke(tag)
        internalState = StateEnum.LOADING
    }
}
