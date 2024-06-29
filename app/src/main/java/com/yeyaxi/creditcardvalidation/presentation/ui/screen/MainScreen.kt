package com.yeyaxi.creditcardvalidation.presentation.ui.screen

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.yeyaxi.creditcardvalidation.R
import com.yeyaxi.creditcardvalidation.domain.CardType
import com.yeyaxi.creditcardvalidation.presentation.ui.theme.CreditCardValidationTheme
import com.yeyaxi.creditcardvalidation.presentation.ui.theme.creditCardFamily
import com.yeyaxi.creditcardvalidation.presentation.ui.viewstate.MainViewState

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun CreditCard(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    onValueChange: (String) -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (cardRef) = createRefs()
        Card(
            modifier = Modifier.constrainAs(cardRef) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    top = parent.top,
                    bottom = parent.bottom
                )
                width = Dimension.ratio("1.586:1")
                height = Dimension.value(200.dp)
            }
        ) {
            ConstraintLayout() {
                val (chipRef, numberRef, iconRef) = createRefs()
                val guide = createGuidelineFromTop(0.5f)
                BasicTextField(
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
                        }
                        .defaultMinSize(
                            minWidth = 180.dp,
                            minHeight = 48.dp
                        ),
                    value = viewState.cardNum,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = TextStyle(fontFamily = creditCardFamily),
                    onValueChange = onValueChange
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
                    CardType.NONE -> R.drawable.ic_bank_card_missing
                }
                Icon(
                    modifier = Modifier.constrainAs(iconRef) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 8.dp)
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CreditCardValidationTheme {
        Greeting("Android")
    }
}

@Preview(name = "portrait", device = "spec:parent=pixel_5")
@Composable
fun PreviewCreditCard() {
    CreditCardValidationTheme {
        CreditCard(viewState = MainViewState(
            cardNum = "4111111111111111",
            cardType = CardType.VISA
        ), onValueChange = {})
    }
}

@Preview(name = "landscape", device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun PreviewCreditCardLandScape() {
    CreditCardValidationTheme {
        CreditCard(viewState = MainViewState(
            cardNum = "4111111111111111",
            cardType = CardType.VISA
        ), onValueChange = {})
    }
}