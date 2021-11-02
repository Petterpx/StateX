package com.petterp.statex.view

import com.petterp.statex.basic.IState

/**
 * 状态页-View 控制接口
 * @author petterp
 */
interface IStateView : IState {
    fun onError(block: stateBlock): StateView
    fun onContent(block: stateBlock): StateView
    fun onLoading(block: stateBlock): StateView
    fun onRefresh(block: StateView.(tag: Any?) -> Unit): StateView
    fun onEmpty(block: stateBlock): StateView
}
