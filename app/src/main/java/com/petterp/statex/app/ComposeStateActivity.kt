package com.petterp.statex.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.petterp.statex.app.ui.theme.StateLayoutXTheme
import com.petterp.statex.compose.*
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
                    val (control, _) = createRefs()
                    var contentState by remember {
                        mutableStateOf(PageState(PageData.Loading))
                    }

                    StateCompose<String>(
                        modifier = Modifier.fillMaxSize(),
                        contentState,
                        loading = {
                            lifecycleScope.launch {
                                delay(2000)
                                contentState = PageState.success("")
                            }
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
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
                    Log.e("petterp", "当前实时状态--${contentState.state}")
                    Row(
                        modifier = Modifier
                            .constrainAs(control) {
                                centerHorizontallyTo(parent)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(bottom = 10.dp)
                    ) {
                        val modifier = Modifier.padding(start = 10.dp)
                        Button(
                            modifier = modifier,
                            onClick = {
                                contentState = PageState(PageData.Success(""))
                            }
                        ) {
                            Text(text = "成功")
                        }
                        Button(
                            modifier = modifier,
                            onClick = {
                                contentState = PageState(PageData.Error())
                                Log.e("petterp", "点击了失败")
                            }
                        ) {
                            Text(text = "错误")
                        }
                        Button(
                            modifier = modifier,
                            onClick = { contentState = PageState(PageData.Loading) }
                        ) {
                            Text(text = "加载中")
                        }
                    }
                }
            }
        }
    }
}
