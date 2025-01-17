package com.yeyaxi.creditcardvalidation.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.yeyaxi.creditcardvalidation.presentation.ui.screen.MainScreen
import com.yeyaxi.creditcardvalidation.presentation.ui.theme.CreditCardValidationTheme
import com.yeyaxi.creditcardvalidation.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreditCardValidationTheme {
                MainScreen(viewModel)
            }
        }
    }
}
