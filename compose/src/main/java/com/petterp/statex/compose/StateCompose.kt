package com.petterp.statex.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

/**
 * ComposeState
 * @author petterp
 */

/** 状态页data */
sealed class PageData {
    data class Success<T>(val t: T) : PageData()
    data class Error(
        val throwable: Throwable? = null,
        val value: Any? = null
    ) : PageData()

    object Loading : PageData()
    data class Empty(val value: Any? = null) : PageData()
}

/** 页面状态 */
class PageState(state: PageData) {

    /** 内部交互的状态 */
    internal var interactionState by mutableStateOf(state)

    /** 供外部获取当前状态 */
    val state: PageData get() = interactionState

    /** 供外部修改当前状态 */
    fun changeState(pageData: PageData) {
        interactionState = pageData
    }

    val isLoading: Boolean
        get() = interactionState is PageData.Loading

    companion object {
        fun loading() = PageData.Loading

        fun <T> success(t: T) = PageData.Success(t)

        fun empty(value: Any? = null) = PageData.Empty(value)

        fun error(
            throwable: Throwable? = null,
            exceptionMessage: Any? = null
        ) = PageData.Error(throwable, exceptionMessage)
    }
}

@Composable
fun rememberPageState(state: PageData = PageData.Loading): PageState {
    return rememberSaveable {
        PageState(state)
    }
}

@Composable
fun <T> StateCompose(
    modifier: Modifier = Modifier,
    pageState: PageState = rememberPageState(),
    loading: () -> Unit,
    loadingComponentBlock: @Composable (BoxScope.() -> Unit)? = StateComposeConfig.loadingComponent,
    emptyComponentBlock: @Composable (BoxScope.(PageData.Empty) -> Unit)? = StateComposeConfig.emptyComponent,
    errorComponentBlock: @Composable (BoxScope.(PageData.Error) -> Unit)? = StateComposeConfig.errorComponent,
    contentComponentBlock: @Composable (BoxScope.(PageData.Success<T>) -> Unit)
) {
    Box(modifier = modifier) {
        when (pageState.interactionState) {
            is PageData.Success<*> -> contentComponentBlock(pageState.interactionState as PageData.Success<T>)
            is PageData.Loading -> {
                loadingComponentBlock?.invoke(this)
                loading.invoke()
            }
            is PageData.Error -> StateBoxCompose({
                pageState.interactionState = PageData.Loading
            }) {
                errorComponentBlock?.invoke(this, pageState.interactionState as PageData.Error)
            }
            is PageData.Empty -> emptyComponentBlock?.invoke(
                this,
                pageState.interactionState as PageData.Empty
            )
        }
    }
}

@Composable
private fun StateBoxCompose(block: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier.clickable {
            block.invoke()
        },
        content = content
    )
}
