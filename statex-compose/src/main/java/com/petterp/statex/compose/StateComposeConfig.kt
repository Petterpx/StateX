package com.petterp.statex.compose

/**
 * state-compose 全局配置
 * @author petterp
 */
class StateComposeConfig {
    internal var loadingComponent: stateComponentBlock = {}
    internal var emptyComponent: stateComponentBlock = {}
    internal var errorComponent: stateComponentBlock = {}
    internal var onContent: stateBlock? = null
    internal var onLoading: stateBlock? = null
    internal var onError: stateBlock? = null
    internal var onEmpty: stateBlock? = null

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
