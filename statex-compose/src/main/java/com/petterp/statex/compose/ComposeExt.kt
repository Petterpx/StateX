package com.petterp.statex.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.cache
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import com.petterp.statex.IState
import com.petterp.statex.StateEnum
import com.petterp.statex.StateX
import com.petterp.statex.StateX.defaultClickTime

internal typealias stateBlock = (tag: Any?) -> Unit
internal typealias stateComponentBlock = @Composable IState.(tag: Any?) -> Unit

/**
 * state全局默认处理方式
 * @author petterp
 */
class ComposeStateConfig {
    internal var loadingComponent: stateComponentBlock = {}
    internal var emptyComponent: stateComponentBlock = {}
    internal var errorComponent: stateComponentBlock = {}
    internal var onContent: stateBlock? = null
    internal var onLoading: stateBlock? = null
    internal var onError: stateBlock? = null
    internal var onEmpty: stateBlock? = null
    internal var enableNullRetry = true
    internal var enableErrorRetry = true

    /** 是否允许空数据时点击重试 */
    fun enableNullRetry(isEnable: Boolean) {
        this.enableErrorRetry = isEnable
    }

    /** 是否加载错误点击重试 */
    fun enableErrorRetry(isEnable: Boolean) {
        this.enableErrorRetry = isEnable
    }

    fun onContent(block: stateBlock) {
        this.onContent = block
    }

    fun onLoading(block: stateBlock) {
        this.onLoading = block
    }

    fun onError(block: stateBlock) {
        this.onError = block
    }

    fun onEmpty(block: stateBlock) {
        this.onEmpty = block
    }

    fun loadingComponent(component: stateComponentBlock) {
        this.loadingComponent = component
    }

    fun errorComponent(component: stateComponentBlock) {
        this.errorComponent = component
    }

    fun emptyComponent(component: stateComponentBlock) {
        this.emptyComponent = component
    }
}

internal val composeConfig by lazy(LazyThreadSafetyMode.PUBLICATION) {
    ComposeStateConfig()
}

/** 配置state-compose的配置 */
fun StateX.composeConfig(config: ComposeStateConfig.() -> Unit) {
    composeConfig.apply(config)
}

/** 生成一个state状态 */
@Composable
fun rememberState(
    stateEnum: StateEnum = StateEnum.CONTENT,
    block: (IStateCompose.() -> Unit)? = null
): IStateCompose =
    currentComposer.cache(false) {
        StateCompose(stateEnum).apply {
            block?.invoke(this)
        }
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
