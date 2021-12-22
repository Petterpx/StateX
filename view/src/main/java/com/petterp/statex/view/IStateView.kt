package com.petterp.statex.view

/**
 * 状态页-View 控制接口
 * @author petterp
 */
interface IStateView {
    /** 当前页面状态 */
    val state: StateEnum

    /** 空数据点击重试 */
    var enableNullRetry: Boolean

    /** 错误点击重试 */
    var enableErrorRetry: Boolean

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

    /** 异常回调 */
    fun onError(block: stateBlock): StateView

    /** 成功回调 */
    fun onContent(block: stateBlock): StateView

    /** loading回调 */
    fun onLoading(block: stateBlock): StateView

    /** 刷新回调 */
    fun onRefresh(block: StateView.(tag: Any?) -> Unit): StateView

    /** 空数据回调 */
    fun onEmpty(block: stateBlock): StateView
}
