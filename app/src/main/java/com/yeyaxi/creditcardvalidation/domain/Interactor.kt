package com.yeyaxi.creditcardvalidation.domain

import kotlinx.coroutines.CancellationException

abstract class Interactor<in P, R> {

    suspend operator fun invoke(
        param: P,
    ): Result<R> {
        return try {
            Result.success(doWork(param))
        } catch (e: CancellationException) {
            throw e
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
    protected abstract suspend fun doWork(param: P): R
}