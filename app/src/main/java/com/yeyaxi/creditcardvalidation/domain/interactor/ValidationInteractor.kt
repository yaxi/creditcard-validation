package com.yeyaxi.creditcardvalidation.domain.interactor

import com.yeyaxi.creditcardvalidation.data.CardType
import com.yeyaxi.creditcardvalidation.domain.Interactor
import javax.inject.Inject
import kotlin.math.pow

class ValidationInteractor @Inject constructor() : Interactor<String, CardType>() {

    override suspend fun doWork(param: String): CardType {
        var n = param.toLong()
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
            throw Throwable("Invalid Number")
        } else {
            return if (amex == 34 || amex == 37) {
                CardType.AMEX
            } else if (discover == 6011) {
                CardType.DISCOVER
            } else if (master in 51..55) {
                CardType.MASTERCARD
            } else if (visa[0] == 4 || visa[1] == 4) {
                CardType.VISA
            } else {
                CardType.UNKNOWN
            }
        }
    }
}