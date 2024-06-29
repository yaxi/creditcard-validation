package com.yeyaxi.creditcardvalidation.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.yeyaxi.creditcardvalidation.R
import com.yeyaxi.creditcardvalidation.domain.CardType
import com.yeyaxi.creditcardvalidation.domain.util.Constants
import com.yeyaxi.creditcardvalidation.presentation.ui.theme.CreditCardValidationTheme
import com.yeyaxi.creditcardvalidation.presentation.ui.theme.creditCardFamily
import com.yeyaxi.creditcardvalidation.presentation.ui.viewstate.MainViewState
import com.yeyaxi.creditcardvalidation.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    MainScreen(
        viewState = viewState,
        onValueChange = { viewModel.validate(it) }
    )
}

@Composable
fun MainScreen(
    viewState: MainViewState,
    onValueChange: (String) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxWidth()
        ) {
            val (cardRef, titleRef, messageRef) = createRefs()
            Text(
                modifier = Modifier.constrainAs(titleRef) {
                    linkTo(
                        top = parent.top,
                        topMargin = 16.dp,
                        start = parent.start,
                        end = parent.end,
                        bottom = cardRef.top
                    )
                    width = Dimension.fillToConstraints
                },
                text = stringResource(id = R.string.title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            CreditCard(
                modifier = Modifier.constrainAs(cardRef) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        top = titleRef.bottom,
                        topMargin = 16.dp,
                        bottom = messageRef.top
                    )
                    width = Dimension.ratio("1.586:1")
                    height = Dimension.value(210.dp)
                },
                viewState = viewState,
                onValueChange = onValueChange
            )
            if (viewState.cardNum.isNotEmpty()) {
                val configuration = LocalConfiguration.current
                Text(
                    modifier = Modifier.constrainAs(messageRef) {
                        top.linkTo(cardRef.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)

                        width = Dimension.fillToConstraints
                    },
                    text = if (viewState.error != null) {
                        stringResource(id = R.string.message_error, viewState.error.message ?: "")
                    } else {
                        stringResource(id = R.string.message, viewState.cardType.name)
                    },
                    style = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        MaterialTheme.typography.titleLarge
                    } else {
                        MaterialTheme.typography.titleMedium
                    },
                    color = if (viewState.error != null) {
                        MaterialTheme.colorScheme.error
                    } else {
                        LocalContentColor.current
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CreditCard(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    onValueChange: (String) -> Unit
) {
    var cardNum by remember {
        mutableStateOf(viewState.cardNum)
    }
    Card(
        modifier = modifier
    ) {
        ConstraintLayout {
            val (chipRef, numberRef, iconRef) = createRefs()
            val guide = createGuidelineFromTop(0.5f)
            TextField(
                modifier = Modifier
                    .constrainAs(numberRef) {
                        top.linkTo(guide)
                        linkTo(
                            start = parent.start,
                            startMargin = 8.dp,
                            end = parent.end,
                            endMargin = 8.dp
                        )
                        width = Dimension.matchParent
                    },
                placeholder = {
                    Text(text = stringResource(id = R.string.placeholder_card_num))
                },
                value = cardNum,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Number
                ),
                isError = viewState.error != null,
                textStyle = TextStyle(
                    letterSpacing = TextUnit(5.9f, TextUnitType.Sp),
                    fontFamily = creditCardFamily,
                ),
                onValueChange = {
                    if (it.length <= Constants.MAX_CARD_NUM_LENGTH) {
                        cardNum = it
                        onValueChange(it)
                    }
                }
            )
            Icon(
                modifier = Modifier.constrainAs(chipRef) {
                    bottom.linkTo(numberRef.top)
                    start.linkTo(parent.start, margin = 8.dp)
                },
                painter = painterResource(id = R.drawable.ic_chip),
                contentDescription = "chip icon",
                tint = Color.Unspecified
            )
            val resource = when (viewState.cardType) {
                CardType.AMEX -> R.drawable.ic_american_express
                CardType.DISCOVER -> R.drawable.ic_discover_card
                CardType.MASTERCARD -> R.drawable.ic_mastercard
                CardType.VISA -> R.drawable.ic_visa
                else -> R.drawable.ic_bank_card_missing
            }
            Icon(
                modifier = Modifier.constrainAs(iconRef) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                    top.linkTo(numberRef.bottom, margin = 8.dp)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                },
                painter = painterResource(id = resource),
                contentDescription = "credit card icon",
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(name = "portrait", device = "spec:parent=pixel_5")
@Composable
fun PreviewMainScreenEmpty() {
    CreditCardValidationTheme {
        MainScreen(viewState = MainViewState(
            cardNum = "",
            cardType = CardType.VISA
        ), onValueChange = {})
    }
}

@Preview(name = "portrait", device = "spec:parent=pixel_5")
@Composable
fun PreviewMainScreen() {
    CreditCardValidationTheme {
        MainScreen(viewState = MainViewState(
            cardNum = "4123456789012345",
            cardType = CardType.VISA
        ), onValueChange = {})
    }
}

@Preview(name = "landscape", device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun PreviewMainScreenLandScape() {
    CreditCardValidationTheme {
        MainScreen(viewState = MainViewState(
            cardNum = "4123456789012345",
            cardType = CardType.VISA
        ), onValueChange = {})
    }
}