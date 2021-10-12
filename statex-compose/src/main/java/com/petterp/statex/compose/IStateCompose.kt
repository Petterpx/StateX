package com.petterp.statex.compose

import androidx.compose.runtime.Stable
import com.petterp.statex.IState
import com.petterp.statex.StateEnum

/**
 * compose状态页控制接口
 * @author petterp
 */
interface IStateCompose : IState {

    @Stable
    override val state: StateEnum

    /** 错误时的回调 */
    fun onError(block: stateBlock)

    /** 加载成功时的回调 */
    fun onContent(block: stateBlock)

    /** 加载中的回调 */
    fun onLoading(block: stateBlock)

    /** 刷新回调 */
    fun onRefresh(block: stateBlock)

    /** 空数据时的回调 */
    fun onEmpty(block: stateBlock)
}
