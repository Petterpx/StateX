package com.petterp.statex.basic

/**
 * 状态枚举
 * [LOADING] 加载中
 * [EMPTY] 空数据
 * [ERROR] 加载错误
 * [CONTENT] 加载成功
 * @author petterp
 */
enum class StateEnum {
    LOADING,
    EMPTY,
    ERROR,
    CONTENT
}
