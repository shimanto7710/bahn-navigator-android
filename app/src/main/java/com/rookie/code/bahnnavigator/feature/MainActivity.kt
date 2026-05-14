package com.rookie.code.bahnnavigator.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rookie.code.bahnnavigator.navigation.BahnNavigatorApp
import com.rookie.code.bahnnavigator.ui.theme.BahnNavigatorTheme
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

@Composable
fun Greeting(name: String) {
    androidx.compose.material3.Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BahnNavigatorTheme {
        _root_ide_package_.com.rookie.code.bahnnavigator.feature.Greeting("Android")
    }
}
