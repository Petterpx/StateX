package com.petterp.statex.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.petterp.statex.StateEnum


/**
 * ComposeState
 * @param stateControl 控制器实例
 * @param loadingWeight loading状态的Widget
 * @param emptyWeight null数据页面的Widget
 * @param errorWeight 加载错误状态的Widget
 * @param contentWeight 加载正常状态的Widget
 * */
@Composable
fun ComposeState(
    modifier: Modifier = Modifier,
    stateControl: IStateCompose,
    loadingWeight: composeStateWidget = composeConfig.loadingWidget,
    emptyWeight: composeStateWidget = composeConfig.emptyWidget,
    errorWeight: composeStateWidget = composeConfig.errorWidget,
    contentWeight: composeStateWidget = composeConfig.contentWidget,
) {
    Box(modifier = modifier) {
        when (stateControl.status) {
            StateEnum.LOADING -> loadingWeight(stateControl)
            StateEnum.EMPTY -> emptyWeight(stateControl)
            StateEnum.ERROR -> errorWeight(stateControl)
            StateEnum.CONTENT -> contentWeight(stateControl)
        }
    }
}
