package com.petterp.statex

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
import com.petterp.statex.compose.StateCompose
import com.petterp.statex.compose.rememberState
import com.petterp.statex.ui.theme.StateLayoutXTheme
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
                        onEmpty {
                        }
                        onContent {
                            Log.e("petterp", "content---tag-$it")
                        }
                        onLoading {
                            Log.e("petterp", "loading---tag-$it")
                        }
                        onRefresh {
                            Log.e("petterp", "refresh---tag-$it")
                            coroutineScope.launch {
                                delay(2000L)
                                showContent()
                            }
                        }
                        onError {
                            Log.e("petterp", "error---tag-$it")
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
                        modifier = Modifier.constrainAs(control) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Button(onClick = { state.showContent() }) {
                            Text(text = "成功")
                        }
                        Button(onClick = { state.showError() }) {
                            Text(text = "错误")
                        }
                        Button(onClick = { state.showLoading() }) {
                            Text(text = "加载中")
                        }
                        Button(onClick = { state.showEmpty() }) {
                            Text(text = "空数据")
                        }
                    }
                }
            }
        }
    }
}
