package com.petterp.state

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.petterp.state.R
import com.petterp.statex.compose.StateCompose
import com.petterp.statex.compose.rememberState
import com.petterp.state.ui.theme.StateLayoutXTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author petterp
 */
class ComposeStateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateLayoutXTheme {
                ConstraintLayout {
                    val (control, content) = createRefs()
                    val coroutineScope = rememberCoroutineScope()
                    val state = rememberState {
                        enableErrorRetry = true
                        enableNullRetry = true
                        onEmpty {
                        }
                        onContent {
                            Log.e("petterp", "content---tag-$it")
                        }
                        onLoading {
                            Log.e("petterp", "loading---tag-$it")
                        }
                        onRefresh {
                            coroutineScope.launch {
                                delay(3000)
                                showContent()
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .constrainAs(content) {
                                centerTo(parent)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        StateCompose(
                            stateControl = state,
                            loadingComponentBlock = {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    alignment = Alignment.Center,
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .height(300.dp),
                                    contentScale = ContentScale.FillHeight,
                                    painter = painterResource(id = R.drawable.ic_state_success),
                                    contentDescription = ""
                                )
                                Text(text = "加载成功", fontSize = 18.sp)
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .constrainAs(control) {
                                centerHorizontallyTo(parent)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(bottom = 10.dp)
                    ) {
                        val modifier = Modifier.padding(start = 10.dp)
                        Button(modifier = modifier, onClick = { state.showContent() }) {
                            Text(text = "成功")
                        }
                        Button(modifier = modifier, onClick = { state.showError("加载错误了,靓仔") }) {
                            Text(text = "错误")
                        }
                        Button(
                            modifier = modifier,
                            onClick = { state.showLoading(refresh = false) }
                        ) {
                            Text(text = "加载中")
                        }
                        Button(
                            modifier = modifier,
                            onClick = { state.showEmpty("空数据了,自定义传递的数据") }
                        ) {
                            Text(text = "空数据")
                        }
                    }
                }
            }
        }
    }
}
