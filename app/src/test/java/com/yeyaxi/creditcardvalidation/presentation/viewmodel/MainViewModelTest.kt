package com.yeyaxi.creditcardvalidation.presentation.viewmodel

import CoroutineTestRule
import app.cash.turbine.test
import com.yeyaxi.creditcardvalidation.domain.CardType
import com.yeyaxi.creditcardvalidation.domain.CoroutineDispatchers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var mainViewModel: MainViewModel
    private val dispatchers by lazy {
        CoroutineDispatchers(
            io = coroutineTestRule.dispatcher,
            default = coroutineTestRule.dispatcher,
            main = coroutineTestRule.dispatcher
        )
    }

    @Before
    fun setup() {
        mainViewModel = MainViewModel(dispatchers)
    }

    @Test
    fun `test validate with empty number expect error is null`() {
        mainViewModel.validate("")
        runTest {
            mainViewModel.viewState.test {
                val item = awaitItem()
                val cardNum = item.cardNum
                val error = item.error
                assertNull(error)
                assertEquals("", cardNum)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `test validate with valid amex number expect cardType is AMEX`() {
        Data.DUMMY_AMEX.forEach {
            mainViewModel.validate(it)
            runTest {
                mainViewModel.viewState.test {
                    val item = awaitItem()
                    val cardType = item.cardType
                    val error = item.error
                    assertNull(error)
                    assertEquals(CardType.AMEX, cardType)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    @Test
    fun `test validate with valid discover number expect cardType is Discover`() {
        mainViewModel.validate(Data.DUMMY_DISCOVER)
        runTest {
            mainViewModel.viewState.test {
                val item = awaitItem()
                val cardType = item.cardType
                val error = item.error
                assertNull(error)
                assertEquals(CardType.DISCOVER, cardType)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `test validate with valid mastercard number expect cardType is Mastercard`() {
        Data.DUMMY_MASTERCARD.forEach {
            mainViewModel.validate(it)
            runTest {
                mainViewModel.viewState.test {
                    val item = awaitItem()
                    val cardType = item.cardType
                    val error = item.error
                    assertNull(error)
                    assertEquals(CardType.MASTERCARD, cardType)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    @Test
    fun `test validate with valid visa number expect cardType is Visa`() {
        Data.DUMMY_VISA.forEach {
            mainViewModel.validate(it)
            runTest {
                mainViewModel.viewState.test {
                    val item = awaitItem()
                    val cardType = item.cardType
                    val error = item.error
                    assertNull(error)
                    assertEquals(CardType.VISA, cardType)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    @Test
    fun `test validate with invalid numbers expect error with Invalid Number cardType is None`() {
        Data.INVALID_NUMBERS.forEach {
            mainViewModel.validate(it)
            println(it)
            runTest {
                mainViewModel.viewState.test {
                    val item = awaitItem()
                    val num = item.cardNum
                    val cardType = item.cardType
                    val error = item.error
                    assertEquals("Invalid Number", error?.message)
                    assertEquals(CardType.NONE, cardType)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    @Test
    fun `test validate with valid number and not from Amex Discover MasterCard or Visa expect cardType is UNKNOWN`() {
        Data.VALID_UNKNOWN_NUMBERS.forEach {
            mainViewModel.validate(it)
            runTest {
                mainViewModel.viewState.test {
                    val item = awaitItem()
                    val cardType = item.cardType
                    val error = item.error
                    assertNull(error)
                    assertEquals(CardType.UNKNOWN, cardType)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    object Data {
        val DUMMY_AMEX = arrayOf(
            "345678901234564",
            "378282246310005"
        )
        const val DUMMY_DISCOVER = "6011111111111117"
        val DUMMY_MASTERCARD = arrayOf(
            "5105105105105100",
            "5200007840000022",
            "5301745529138831",
            "5404000000000001",
            "5555558265554449"
        )
        val DUMMY_VISA = arrayOf(
            "4007000000027",
            "4111111111111111"
        )
        val INVALID_NUMBERS = arrayOf(
            "34567890123451",
            "37",
            "37828224631000",
            "398282246310005",
            "6011",
            "601111111111",
            "5005105105105100",
            "51",
            "52",
            "53",
            "54",
            "55",
            "5105105105105107",
            "5200007840000021",
            "5301745529138832",
            "5404000000000002",
            "5555558265554440",
            "4007000000028",
            "4",
            "4711111111111111",
        )
        val VALID_UNKNOWN_NUMBERS = arrayOf(
            "34", // incomplete
            "3056930009020004", // Diners Club
            "3530111333300000", // JCB
            "6200000000000005" // UnionPay
        )
    }
}