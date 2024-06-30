package com.yeyaxi.creditcardvalidation.domain.interactor

import com.yeyaxi.creditcardvalidation.domain.CardType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ValidationInteractorTest {

    private lateinit var interactor: ValidationInteractor

    @Before
    fun setup() {
        interactor = ValidationInteractor()
    }

    @Test
    fun `test validate with empty number expect error is null`() {
        runTest {
            val result = interactor.invoke("")
            assertTrue(result.isSuccess)
            assertEquals(CardType.NONE, result.getOrNull())
        }
    }

    @Test
    fun `test validate with valid amex number expect cardType is AMEX`() {
        runTest {
            Data.DUMMY_AMEX.forEach {
                val result = interactor.invoke(it)
                assertTrue(result.isSuccess)
                assertEquals(CardType.AMEX, result.getOrNull())
            }
        }
    }

    @Test
    fun `test validate with valid discover number expect cardType is Discover`() {
        runTest {
            val result = interactor.invoke(Data.DUMMY_DISCOVER)
            assertTrue(result.isSuccess)
            assertEquals(CardType.DISCOVER, result.getOrNull())
        }
    }

    @Test
    fun `test validate with valid mastercard number expect cardType is Mastercard`() {
        runTest {
            Data.DUMMY_MASTERCARD.forEach {
                val result = interactor.invoke(it)
                assertTrue(result.isSuccess)
                assertEquals(CardType.MASTERCARD, result.getOrNull())
            }
        }
    }

    @Test
    fun `test validate with valid visa number expect cardType is Visa`() {
        runTest {
            Data.DUMMY_VISA.forEach {
                val result = interactor.invoke(it)
                assertTrue(result.isSuccess)
                assertEquals(CardType.VISA, result.getOrNull())
            }
        }
    }

    @Test
    fun `test validate with invalid numbers expect error with Invalid Number`() {
        runTest {
            Data.INVALID_NUMBERS.forEach {
                val result = interactor.invoke(it)
                assertFalse(result.isSuccess)
                assertTrue(result.isFailure)
                val t = result.exceptionOrNull()
                assertEquals("Invalid Number", t?.message)
                assertNull(result.getOrNull())
            }
        }
    }

    @Test
    fun `test validate with valid number and not from Amex Discover MasterCard or Visa expect cardType is UNKNOWN`() {
        runTest {
            Data.VALID_UNKNOWN_NUMBERS.forEach {
                val result = interactor.invoke(it)
                assertTrue(result.isSuccess)
                assertEquals(CardType.UNKNOWN, result.getOrNull())
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