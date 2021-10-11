package com.petterp.state.view

import com.petterp.state.IState

/**
 * 状态页-View 控制接口
 * @author petterp
 */
interface IStateView : IState<StateView> {
    fun onError(block: stateBlock): StateView
    fun onContent(block: stateBlock): StateView
    fun onLoading(block: stateBlock): StateView
    fun onRefresh(block: StateView.(tag: Any?) -> Unit): StateView
    fun onEmpty(block: stateBlock): StateView

    fun showContent(tag: Any? = null)
    fun showError(tag: Any? = null)
    fun showEmpty(tag: Any? = null)
    fun showLoading(
        tag: Any? = null,
        silent: Boolean = false,
        refresh: Boolean = true
    )
}
