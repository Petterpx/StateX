package com.petterp.statex

import android.app.Application
import androidx.compose.material.Text
import com.petterp.statex.compose.composeConfig
import com.petterp.statex.view.viewConfig

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