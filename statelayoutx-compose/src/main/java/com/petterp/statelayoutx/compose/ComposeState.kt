package com.petterp.statelayoutx.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.petterp.statelayoutx.StateEnum

/**
 *
 * @author petterp
 */
typealias onStateBack = (Any?) -> Unit
typealias composeStateWidget = @Composable (IStateControl) -> Unit

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
    stateControl: IStateControl,
    loadingWeight: composeStateWidget = ComposeStateConfig.loadingWidget,
    emptyWeight: composeStateWidget = ComposeStateConfig.emptyWidget,
    errorWeight: composeStateWidget = ComposeStateConfig.errorWidget,
    contentWeight: composeStateWidget = ComposeStateConfig.contentWidget
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
