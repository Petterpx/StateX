package com.petterp.statex.app.simple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author petterp
 */
class CustomViewModel : ViewModel() {

    fun getData() {
        viewModelScope.launch {
            delay(3000)
            toStateSuccess("加载成功")
        }
    }

    fun toStateSuccess(any: Any?) {
    }

    fun toStateError(any: Any?) {
    }

    fun toStateEmpty(any: Any?) {
    }

    fun toStateLoading(
        tag: Any? = null,
        silent: Boolean = false,
        refresh: Boolean = true
    ) {
    }
}
