package com.petterp.statelayoutx

import android.app.Application
import com.petterp.statelayoutx.compose.StateConfig

/**
 *
 * @author petterp
 */
class CustomApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        StateConfig.contentWidget
    }
}
