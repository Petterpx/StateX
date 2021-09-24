package com.petterp.statelayoutx

import android.app.Application
import androidx.compose.material.Text
import com.petterp.statelayoutx.compose.ComposeStateConfig

/**
 *
 * @author petterp
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ComposeStateConfig.apply {
            errorWidget = {
                Text(text = "全局加载错误")
            }
            emptyWidget = {
                Text(text = "全局null数据")
            }
        }
    }
}
