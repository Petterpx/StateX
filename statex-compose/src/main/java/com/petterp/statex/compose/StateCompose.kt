package com.petterp.statex.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.petterp.statex.StateEnum

/**
 * ComposeState
 * @param stateControl 控制器实例
 * @param loadingComponentBlock loading状态的Component
 * @param emptyComponentBlock null数据页面的Component
 * @param errorComponentBlock 加载错误状态的Component
 * @param contentComponentBlock 加载正常状态的Component
 * */
@Composable
fun StateCompose(
    stateControl: IStateCompose,
    loadingComponentBlock: stateComponentBlock = composeConfig.loadingComponent,
    emptyComponentBlock: stateComponentBlock = composeConfig.emptyComponent,
    errorComponentBlock: stateComponentBlock = composeConfig.errorComponent,
    contentComponentBlock: stateComponentBlock = composeConfig.contentComponent,
) {
    when (stateControl.state) {
        StateEnum.LOADING -> loadingComponentBlock(stateControl)
        StateEnum.CONTENT -> contentComponentBlock(stateControl)
        StateEnum.EMPTY -> StateBoxComposeClick(block = { stateControl.showLoading() }) {
            emptyComponentBlock(stateControl)
        }
        StateEnum.ERROR -> StateBoxComposeClick(block = { stateControl.showLoading() }) {
            errorComponentBlock(stateControl)
        }
    }
}

@Composable
fun StateBoxComposeClick(block: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier.clickOne(indication = null) {
            block.invoke()
        },
        content = content
    )
}
