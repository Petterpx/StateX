package com.petterp.statelayoutx.compose

import com.petterp.statelayoutx.StateEnum

/**
 * 控制器具体行为接口
 * @author petterp
 */
interface IStateControl {
    val status: StateEnum
    fun onError(block: onStateBack)
    fun onContent(block: onStateBack)
    fun onLoading(block: onStateBack)
    fun onRefresh(block: onStateBack)
    fun onEmpty(block: onStateBack)

    fun showContent(tag: Any? = null): IStateControl
    fun showError(tag: Any? = null): IStateControl
    fun showEmpty(tag: Any? = null): IStateControl
    fun showLoading(
        tag: Any? = null,
        silent: Boolean = false,
        refresh: Boolean = true
    ): IStateControl
}
