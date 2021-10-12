package com.petterp.statex

/**
 * 控制器具体行为接口
 * @author petterp
 */
interface IState {

    /** 当前页面状态 */
    val state: StateEnum

    /** 显示加载成功
     * @param [tag] 可以传递任意数据,会在回调处收到
     * */
    fun showContent(tag: Any? = null)

    /** 显示加载失败
     * @param [tag] 可以传递任意数据,会在回调处收到
     * */
    fun showError(tag: Any? = null)

    /** 显示null数据页
     * @param [tag] 可以传递任意数据,会在回调处收到
     * */
    fun showEmpty(tag: Any? = null)

    /** 显示加载中
     * @param [tag] 可以传递任意数据,会在回调处收到
     * @param [silent] 是否静默加载,即不显示loading
     * @param [refresh] 是否触发刷新回调
     * */
    fun showLoading(
        tag: Any? = null,
        silent: Boolean = false,
        refresh: Boolean = true
    )
}
