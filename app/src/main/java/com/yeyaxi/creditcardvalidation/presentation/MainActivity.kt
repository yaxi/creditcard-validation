package com.yeyaxi.creditcardvalidation.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yeyaxi.creditcardvalidation.presentation.ui.screen.MainScreen
import com.yeyaxi.creditcardvalidation.presentation.ui.theme.CreditCardValidationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreditCardValidationTheme {
                MainScreen()
            }
        }
    }
}
