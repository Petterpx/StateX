package com.petterp.statex.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.petterp.statex.basic.StateEnum
import com.petterp.statex.basic.StateX

/**
 * Compose 状态控制器实例,对于外部,通过[IStateCompose]控制
 * @author petterp
 */
class StateComposeImpl constructor(stateEnum: StateEnum = StateEnum.CONTENT) : IStateCompose {

    private var onRefresh: stateBlock? = null
    private var onEmpty: stateBlock? = composeConfig.onEmpty
    private var onContent: stateBlock? = composeConfig.onContent
    private var onError: stateBlock? = composeConfig.onError
    private var onLoading: stateBlock? = composeConfig.onLoading

    /** 当前内部可变状态 */
    private var _internalState by mutableStateOf(StateEnum.CONTENT)

    /** 当前状态内部缓存的tag */
    private var _internalTag: Any?

    init {
        _internalState = stateEnum
        _internalTag = null
    }

    override val state: StateEnum
        get() = _internalState
    override val tag: Any?
        get() = _internalTag
    override var enableNullRetry: Boolean = StateX.enableNullRetry
    override var enableErrorRetry: Boolean = StateX.enableNullRetry

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
        _internalState = newState
        _internalTag = tag
    }
}
