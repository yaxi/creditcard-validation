package com.yeyaxi.creditcardvalidation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeyaxi.creditcardvalidation.domain.CardType
import com.yeyaxi.creditcardvalidation.domain.util.CoroutineDispatchers
import com.yeyaxi.creditcardvalidation.domain.interactor.ValidationInteractor
import com.yeyaxi.creditcardvalidation.presentation.ui.viewstate.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val validationInteractor: ValidationInteractor
): ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    fun validate(num: String) {
        viewModelScope.launch(dispatchers.main) {
            setState {
                if (num.isEmpty()) {
                    copy(cardNum = "", error = null)
                } else {
                    copy(cardNum = num)
                }
            }

        }
        if (num.isNotEmpty()) {
            viewModelScope.launch(dispatchers.default) {
                val result = validationInteractor.invoke(num)
                result.onSuccess {
                    viewModelScope.launch(dispatchers.main) {
                        setState { copy(cardType = it, error = null) }
                    }
                }
                result.onFailure {
                    viewModelScope.launch(dispatchers.main) {
                        setState { copy(cardType = CardType.NONE, error = it) }
                    }
                }
            }
        }
    }

    private fun setState(reducer: MainViewState.() -> MainViewState) {
        viewModelScope.launch(dispatchers.main) {
            _viewState.value = reducer(_viewState.value)
        }
    }
}