package com.petterp.state

import android.app.Application
import androidx.compose.material.Text
import com.petterp.state.compose.composeConfig
import com.petterp.state.view.viewConfig

/**
 *
 * @author petterp
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StateX.apply {
            composeConfig {
                errorWidget = {
                    Text(text = "全局加载错误")
                }
                emptyWidget = {
                    Text(text = "全局null数据")
                }
            }
            viewConfig {
            }
        }
    }
}
