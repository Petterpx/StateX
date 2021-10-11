package com.petterp.statex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.petterp.statex.compose.ComposeState
import com.petterp.statex.compose.createComposeState
import com.petterp.statex.ui.theme.StateLayoutXTheme

/**
 *
 * @author petterp
 */
class ComposeStateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateLayoutXTheme {
                Column {
                    var state by remember {
                        mutableStateOf(
                            StateX.createComposeState().apply {
                                onEmpty {
                                    Log.e("petterp", "null---tag-$it")
                                }
                                onContent {
                                    Log.e("petterp", "content---tag-$it")
                                }
                                onLoading {
                                    Log.e("petterp", "loading---tag-$it")
                                }
                                onRefresh {
                                    Log.e("petterp", "refresh---tag-$it")
                                }
                                onError {
                                    Log.e("petterp", "error---tag-$it")
                                }
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        ComposeState(
                            stateControl = state,
                            modifier = Modifier.fillMaxSize(),
                            loadingWeight = {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        ) {
                            Text(text = "加载ok", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    Column() {
                        Button(onClick = { state = state.showContent() }) {
                            Text(text = "加载正确")
                        }
                        Button(onClick = { state = state.showError() }) {
                            Text(text = "显示错误")
                        }
                        Button(onClick = { state = state.showLoading() }) {
                            Text(text = "加载中")
                        }
                        Button(onClick = { state = state.showEmpty() }) {
                            Text(text = "null数据")
                        }
                    }
                }
            }
        }
    }
}
