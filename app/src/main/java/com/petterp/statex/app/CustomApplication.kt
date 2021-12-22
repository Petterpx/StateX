package com.petterp.statex.app

import android.app.Application
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.petterp.statex.compose.StateComposeConfig
import com.petterp.statex.view.StateViewConfig

/**
 *
 * @author petterp
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StateViewConfig.apply {
            // 更改默认防重复点击时间为1000L
            defaultClickTime = 1000L
            // 设置view-state的配置
            emptyLayout = R.layout.item_state_empty
            setRetryIds(R.id.errorId)
            onError {
                Toast.makeText(this@CustomApplication, "全局错误提示-view", Toast.LENGTH_SHORT).show()
            }
            // 设置compose-state的配置
        }
        StateComposeConfig.apply {
            // 将tag传递出来,对于compose而言,我们有时需要对界面进行重绘,所以携带了传递的数据,以便做自定义处理
            errorComponent {
                // Compose中插入原生
                AndroidView(
                    {
                        View.inflate(it, R.layout.item_state_error, null).apply {
                            findViewById<TextView>(R.id.tvErrorHint).text =
                                tag?.toString() ?: getString(R.string.tv_status_error_hint)
                        }
                    },
                    Modifier
                        .fillMaxSize()
                )
            }
            loadingComponent {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(
                            Alignment.Center
                        )
                )
            }
            emptyComponent {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                ) {
                    Image(
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .height(300.dp),
                        contentScale = ContentScale.FillHeight,
                        painter = painterResource(id = R.drawable.ic_state_empty),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}
