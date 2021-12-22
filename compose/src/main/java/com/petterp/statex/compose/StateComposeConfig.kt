package com.petterp.statex.compose

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

/**
 * state-compose 全局配置
 * @author petterp
 */
object StateComposeConfig {
    internal var loadingComponent: @Composable (BoxScope.() -> Unit)? = null
    internal var emptyComponent: @Composable (BoxScope.(PageData.Empty) -> Unit)? = null
    internal var errorComponent: @Composable (BoxScope.(PageData.Error) -> Unit)? = null

    fun loadingComponent(component: @Composable (BoxScope.() -> Unit)? = null) {
        this.loadingComponent = component
    }

    fun errorComponent(component: @Composable (BoxScope.(PageData.Error) -> Unit)? = null) {
        this.errorComponent = component
    }

    fun emptyComponent(component: @Composable (BoxScope.(PageData.Empty) -> Unit)? = null) {
        this.emptyComponent = component
    }
}
