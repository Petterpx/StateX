package com.petterp.state

/**
 * 控制器具体行为接口
 * @author petterp
 */
interface IState<T> {
    var status: StateEnum
}
