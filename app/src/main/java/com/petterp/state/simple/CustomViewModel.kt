package com.petterp.state.simple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petterp.statex.compose.lazyState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author petterp
 */
class CustomViewModel : ViewModel() {

    val state by lazyState()

    fun getData() {
        viewModelScope.launch {
            delay(3000)
            toStateSuccess("加载成功")
        }
    }

    fun toStateSuccess(any: Any?) {
        state.showContent(any)
    }

    fun toStateError(any: Any?) {
        state.showError(any)
    }

    fun toStateEmpty(any: Any?) {
        state.showEmpty(any)
    }

    fun toStateLoading(
        tag: Any? = null,
        silent: Boolean = false,
        refresh: Boolean = true
    ) {
        state.showLoading()
    }
}
