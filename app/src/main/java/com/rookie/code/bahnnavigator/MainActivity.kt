package com.rookie.code.bahnnavigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rookie.code.bahnnavigator.core.ui.theme.BahnNavigatorTheme
import com.rookie.code.bahnnavigator.navigation.BahnNavigatorApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BahnNavigatorTheme {
                BahnNavigatorApp()
            }
        }
    }
}
