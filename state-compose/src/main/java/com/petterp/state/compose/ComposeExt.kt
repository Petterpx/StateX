package com.petterp.state.compose

import androidx.compose.runtime.Composable
import com.petterp.state.StateEnum
import com.petterp.state.StateX

internal typealias stateBlock = (Any?) -> Unit
internal typealias composeStateWidget = @Composable (IStateCompose) -> Unit

/**
 * state全局默认处理方式
 * @author petterp
 */
class ComposeStateConfig {
    var loadingWidget: composeStateWidget = {}
    var emptyWidget: composeStateWidget = {}
    var errorWidget: composeStateWidget = {}
    var contentWidget: composeStateWidget = {}
}

internal val composeConfig by lazy(LazyThreadSafetyMode.PUBLICATION) {
    ComposeStateConfig()
}

/** 配置state-compose的配置 */
fun StateX.composeConfig(config: ComposeStateConfig.() -> Unit) {
    composeConfig.apply(config)
}

/** 生成一个compose状态 */
fun StateX.createComposeState(status: StateEnum = StateEnum.CONTENT): IStateCompose =
    StateComposeImpl(status)
