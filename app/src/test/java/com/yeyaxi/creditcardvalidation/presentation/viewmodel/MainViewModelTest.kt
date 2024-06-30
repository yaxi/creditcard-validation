package com.yeyaxi.creditcardvalidation.presentation.viewmodel

import CoroutineTestRule
import app.cash.turbine.test
import com.yeyaxi.creditcardvalidation.data.CardType
import com.yeyaxi.creditcardvalidation.domain.interactor.ValidationInteractor
import com.yeyaxi.creditcardvalidation.domain.util.CoroutineDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
    private lateinit var interactor: ValidationInteractor

    @Before
    fun setup() {
        interactor = mockk()
        mainViewModel = MainViewModel(
            dispatchers = dispatchers,
            validationInteractor = interactor
        )
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
    fun `test validate when validator returns success expect error is null and cardType is set`() {
        runTest {
            coEvery { interactor.invoke(any()) } returns Result.success(CardType.AMEX)
            mainViewModel.validate(Data.DUMMY_AMEX)
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

    @Test
    fun `test validate with valid discover number expect cardType is Discover`() {
        runTest {
            coEvery { interactor.invoke(any()) } returns Result.success(CardType.DISCOVER)
            mainViewModel.validate(Data.DUMMY_DISCOVER)
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
        runTest {
            coEvery { interactor.invoke(any()) } returns Result.success(CardType.MASTERCARD)
            mainViewModel.validate(Data.DUMMY_MASTERCARD)
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

    @Test
    fun `test validate with valid visa number expect cardType is Visa`() {
        runTest {
            coEvery { interactor.invoke(any()) } returns Result.success(CardType.VISA)
            mainViewModel.validate(Data.DUMMY_VISA)
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

    @Test
    fun `test validate with invalid numbers expect error with Invalid Number cardType is None`() {
        runTest {
            coEvery { interactor.invoke(any()) } returns Result.failure(Throwable("Invalid Number"))
            mainViewModel.validate("1234")
            mainViewModel.viewState.test {
                val item = awaitItem()
                val cardType = item.cardType
                val error = item.error
                assertEquals("Invalid Number", error?.message)
                assertEquals(CardType.NONE, cardType)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `test validate with valid number and not from Amex Discover MasterCard or Visa expect cardType is UNKNOWN`() {
        runTest {
            coEvery { interactor.invoke(any()) } returns Result.success(CardType.UNKNOWN)
            mainViewModel.validate(Data.VALID_UNKNOWN_NUMBERS)
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

    object Data {

        const val DUMMY_AMEX = "345678901234564"
        const val DUMMY_DISCOVER = "6011111111111117"
        const val DUMMY_MASTERCARD = "5105105105105100"
        const val DUMMY_VISA = "4007000000027"
        const val VALID_UNKNOWN_NUMBERS = "3056930009020004"
    }
}