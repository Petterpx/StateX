package com.petterp.statex.compose

import androidx.compose.runtime.*
import com.petterp.statex.StateEnum

/**
 * Compose 状态控制器实例,对于外部,通过[IStateCompose]控制
 * @author petterp
 */
class StateCompose internal constructor(stateEnum: StateEnum) : IStateCompose {

    /** 当前状态对应的value */
    private var _stateData: Any? = null
    private var _internalState by mutableStateOf(StateEnum.CONTENT)

    init {
        _internalState = stateEnum
    }

    /** 当前内部状态,外部不应感知到其存在 */
    private var onEmpty: stateBlock? = composeConfig.onEmpty
    private var onContent: stateBlock? = composeConfig.onContent
    private var onError: stateBlock? = composeConfig.onError
    private var onLoading: stateBlock? = composeConfig.onLoading
    private var onRefresh: stateBlock? = null

    override val state: StateEnum
        get() = _internalState
    override val stateData: Any?
        get() = _stateData
    override var enableNullRetry: Boolean = composeConfig.enableNullRetry
    override var enableErrorRetry: Boolean = composeConfig.enableNullRetry

    override fun onError(block: stateBlock) {
        this.onError = block
    }

    override fun onContent(block: stateBlock) {
        this.onContent = block
    }

    override fun onRefresh(block: stateBlock) {
        this.onRefresh = block
    }

    override fun onLoading(block: stateBlock) {
        this.onLoading = block
    }

    override fun onEmpty(block: stateBlock) {
        this.onEmpty = block
    }

    override fun showError(tag: Any?) {
        onError?.invoke(tag)
        newState(StateEnum.ERROR, tag)
    }

    override fun showContent(tag: Any?) {
        onContent?.invoke(tag)
        newState(StateEnum.CONTENT, tag)
    }

    override fun showEmpty(tag: Any?) {
        onEmpty?.invoke(tag)
        newState(StateEnum.EMPTY, tag)
    }

    override fun showLoading(
        tag: Any?,
        silent: Boolean,
        refresh: Boolean
    ) {
        onLoading?.invoke(tag)
        if (refresh) onRefresh?.invoke(tag)
        if (!silent) {
            newState(StateEnum.LOADING, tag)
        }
    }

    private fun newState(newState: StateEnum, tag: Any?) {
        _stateData = tag
        _internalState = newState
    }
}
