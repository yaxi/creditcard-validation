package com.yeyaxi.creditcardvalidation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeyaxi.creditcardvalidation.domain.CardType
import com.yeyaxi.creditcardvalidation.domain.CoroutineDispatchers
import com.yeyaxi.creditcardvalidation.presentation.ui.viewstate.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
): ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        setState { copy(error = throwable) }
    }

    fun validate(num: String) {
        viewModelScope.launch(dispatchers.main + exceptionHandler) {
            setState {
                if (num.isEmpty()) {
                    copy(cardNum = "", error = null)
                } else {
                    copy(cardNum = num)
                }
            }

        }
        if (num.isNotEmpty()) {
            viewModelScope.launch(dispatchers.default + exceptionHandler) {
                var n = num.toLong()
                val amex = (n / 10.0.pow(13.0)).toInt()
                val discover = (n / 10.0.pow(12.0)).toInt()
                val master = (n / 10.0.pow(14.0)).toInt()
                val visa = IntArray(2) { 0 }
                visa[0] = (n / 10.0.pow(12.0)).toInt()
                visa[1] = (n / 10.0.pow(15.0)).toInt()

                var count = 0
                var sum = 0
                while (n > 0) {
                    var digit = (n % 10).toInt()
                    n /= 10
                    count++
                    if (count % 2 == 0) {
                        digit *= 2
                        if (digit > 9) {
                            digit -= 9
                        }
                    }
                    sum += digit
                }

                if (sum % 10 != 0) {
                    setState { copy(error = Throwable("Invalid Number"), cardType = CardType.NONE) }
                } else {
                    if (amex == 34 || amex == 37) {
                        setState { copy(cardType = CardType.AMEX, error = null) }
                    } else if (discover == 6011) {
                        setState { copy(cardType = CardType.DISCOVER, error = null) }
                    } else if (master in 51..55) {
                        setState { copy(cardType = CardType.MASTERCARD, error = null) }
                    } else if (visa[0] == 4 || visa[1] == 4) {
                        setState { copy(cardType = CardType.VISA, error = null) }
                    } else {
                        setState { copy(cardType = CardType.UNKNOWN, error = null) }
                    }
                }
            }
        }
    }

    private fun setState(reducer: MainViewState.() -> MainViewState) {
        viewModelScope.launch(dispatchers.main + exceptionHandler) {
            _viewState.value = reducer(_viewState.value)
        }
    }
}