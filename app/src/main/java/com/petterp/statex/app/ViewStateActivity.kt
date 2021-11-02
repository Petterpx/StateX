package com.petterp.statex.app

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.petterp.statex.view.StateView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 传统视图的状态
 * @author petterp
 */
class ViewStateActivity : AppCompatActivity(), View.OnClickListener {

    private val stateView: StateView by lazy {
        findViewById(R.id.stateView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_state)
        stateView.onRefresh {
            lifecycleScope.launch {
                delay(1000)
                showContent()
            }
        }.showLoading()
        stateView.apply {
            emptyLayout = R.layout.item_state_empty
            errorLayout = R.layout.item_state_error
            loadingLayout = R.layout.item_state_loading
        }
        findViewById<Button>(R.id.btnContent).setOnClickListener(this)
        findViewById<Button>(R.id.btnEmpty).setOnClickListener(this)
        findViewById<Button>(R.id.btnError).setOnClickListener(this)
        findViewById<Button>(R.id.btnLoading).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnContent -> stateView.showContent()
            R.id.btnLoading -> stateView.showLoading(refresh = false)
            R.id.btnError -> stateView.showError()
            R.id.btnEmpty -> stateView.showEmpty()
        }
    }
}
