package com.petterp.statex.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

/**
 *
 * @author petterp
 */

/** 状态页data */
sealed class PageData<out T> {
    data class Success<T>(val t: T? = null) : PageData<T>()
    data class Error(
        val throwable: Throwable? = null,
        val value: Any? = null
    ) : PageData<Nothing>()

    object Loading : PageData<Nothing>()
    data class Empty(val value: Any? = null) : PageData<Nothing>()
}

/** 页面状态 */
class PageState<T>(state: PageData<T>) {

    /** 内部交互的状态 */
    internal var interactionState by mutableStateOf(state)

    /** 供外部获取当前状态 */
    val state: PageData<T>
        get() = interactionState

    val isLoading: Boolean
        get() = interactionState is PageData.Loading
}

@Composable
fun <T> rememberPageState(state: PageData<T> = PageData.Loading): PageState<T> {
    return rememberSaveable {
        PageState(state)
    }
}

@Composable
fun <T> ComposeState(
    modifier: Modifier,
    pageState: PageState<T> = rememberPageState(),
    loading: suspend () -> PageData<T>,
    loadingComponentBlock: @Composable (BoxScope.() -> Unit)? = StateComposeConfig.loadingComponent,
    emptyComponentBlock: @Composable (BoxScope.(PageData.Empty) -> Unit)? = StateComposeConfig.emptyComponent,
    errorComponentBlock: @Composable (BoxScope.(PageData.Error) -> Unit)? = StateComposeConfig.errorComponent,
    contentComponentBlock: @Composable (BoxScope.(PageData.Success<T>) -> Unit)
) {
    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {
        when (pageState.interactionState) {
            is PageData.Success -> contentComponentBlock(pageState.interactionState as PageData.Success<T>)
            is PageData.Loading -> {
                loadingComponentBlock?.invoke(this)
                scope.launch {
                    pageState.interactionState = loading.invoke()
                }
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
fun <T> ComposeProducerState(modifier: Modifier, obj: suspend () -> T) {
    var key by remember {
        mutableStateOf(true)
    }
    val state by produceState<PageData<T>>(initialValue = PageData.Loading, key) {
        if (value == PageData.Loading) {
            value = try {
                PageData.Success(obj())
            } catch (e: Throwable) {
                PageData.Error(e)
            }
        }
    }

    Box(modifier = modifier) {
        when (state) {
            is PageData.Success -> {}
            is PageData.Loading -> {}
            is PageData.Error -> {
                // ...
                key = !key
                // ...
            }
            is PageData.Empty -> {}
        }
    }
}

@Composable
fun StateBoxCompose(block: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier.clickable {
            block.invoke()
        },
        content = content
    )
}
