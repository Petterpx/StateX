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
    contentComponentBlock: stateComponentBlock,
) {
    val data = stateControl.stateData
    when (stateControl.state) {
        StateEnum.LOADING -> loadingComponentBlock(stateControl, data)
        StateEnum.CONTENT -> contentComponentBlock(stateControl, data)
        StateEnum.EMPTY -> if (stateControl.enableNullRetry) {
            StateBoxComposeClick(block = { stateControl.showLoading() }) {
                emptyComponentBlock(stateControl, data)
            }
        } else emptyComponentBlock(stateControl, data)
        StateEnum.ERROR -> if (stateControl.enableErrorRetry) {
            StateBoxComposeClick(block = {
                stateControl.showLoading(data)
            }) {
                errorComponentBlock(stateControl, data)
            }
        } else errorComponentBlock(stateControl, data)
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
