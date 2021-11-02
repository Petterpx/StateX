package com.petterp.statex.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.cache
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.ViewModel
import com.petterp.statex.basic.IState
import com.petterp.statex.basic.StateEnum
import com.petterp.statex.basic.StateX
import com.petterp.statex.basic.StateX.defaultClickTime

internal typealias stateBlock = (tag: Any?) -> Unit
internal typealias stateComponentBlock = @Composable IState.(tag: Any?) -> Unit

/** 内部使用的StateCompose配置 */
internal val composeConfig by lazy(LazyThreadSafetyMode.PUBLICATION) {
    StateComposeConfig()
}

/** 配置state-compose的配置 */
fun StateX.composeConfig(config: StateComposeConfig.() -> Unit) {
    composeConfig.apply(config)
}

/**
 * ComposeState
 * @param stateControl 控制器实例
 * @param lazyLoad 是否延迟load
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
    when (stateControl.state) {
        StateEnum.LOADING -> loadingComponentBlock(stateControl, null)
        StateEnum.CONTENT -> contentComponentBlock(stateControl, null)
        StateEnum.EMPTY -> if (stateControl.enableNullRetry) {
            StateBoxComposeClick(block = { stateControl.showLoading() }) {
                emptyComponentBlock(stateControl, null)
            }
        } else emptyComponentBlock(stateControl, null)
        StateEnum.ERROR -> if (stateControl.enableErrorRetry) {
            StateBoxComposeClick(block = {
                stateControl.showLoading(null)
            }) {
                errorComponentBlock(stateControl, null)
            }
        } else errorComponentBlock(stateControl, null)
    }
}

/** 在ViewModel中生成一个 IStateCompose
 * @param stateEnum 默认的状态
 * */
inline fun ViewModel.lazyState(
    stateEnum: StateEnum = StateEnum.CONTENT,
    crossinline obj: StateComposeImpl.() -> Unit = {}
): Lazy<IStateCompose> = lazy(LazyThreadSafetyMode.PUBLICATION) {
    StateComposeImpl(stateEnum).apply(obj)
}

/**
 * 当state在ViewModel中缓存时,可以使用这个方法便于对state做初始化相关
 * @param composeState 要记住的状态State
 * */
@Composable
inline fun rememberState(
    composeState: IStateCompose,
    crossinline block: IStateCompose.() -> Unit = {}
): IStateCompose = currentComposer.cache(false) {
    composeState.apply(block)
}

/**
 * 记录state的状态,直接生成一个新的IStateCompose
 * @param stateEnum 默认的状态
 * @param block 对于IStateCompose的回调使用
 * */
@Composable
inline fun rememberState(
    stateEnum: StateEnum = StateEnum.CONTENT,
    crossinline block: IStateCompose.() -> Unit = {}
): IStateCompose = currentComposer.cache(false) {
    StateComposeImpl(stateEnum).apply(block)
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

/** compose-防重复点击 */
@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun Modifier.clickOne(
    retryTime: Long = defaultClickTime,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickableOne"
        properties["retryTime"] = retryTime
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    var time = remember { 0L }
    val c = remember {
        {
            val newTime = System.currentTimeMillis()
            if (newTime - time > retryTime) {
                time = newTime
                onClick()
            }
        }
    }
    clickable(
        remember { MutableInteractionSource() },
        indication,
        enabled,
        onClickLabel,
        role,
        c
    )
}
