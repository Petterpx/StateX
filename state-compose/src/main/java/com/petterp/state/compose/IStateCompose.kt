package com.petterp.state.compose

import com.petterp.state.IState

/**
 * compose状态页控制接口
 * @author petterp
 */
interface IStateCompose : IState<IStateCompose> {
    fun onError(block: stateBlock)
    fun onContent(block: stateBlock)
    fun onLoading(block: stateBlock)
    fun onRefresh(block: stateBlock)
    fun onEmpty(block: stateBlock)

    fun showContent(tag: Any? = null): IStateCompose
    fun showError(tag: Any? = null): IStateCompose
    fun showEmpty(tag: Any? = null): IStateCompose
    fun showLoading(
        tag: Any? = null,
        silent: Boolean = false,
        refresh: Boolean = true
    ): IStateCompose
}
