package com.yeyaxi.creditcardvalidation.presentation.ui.viewstate

import com.yeyaxi.creditcardvalidation.data.CardType

data class MainViewState(
    val cardNum: String = "",
    val cardType: CardType = CardType.NONE,
    val error: Throwable? = null
)
