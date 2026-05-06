package com.ivarvisser.cineapp.utils


/**
 * A sealed class representing the result of an operation, which can either be a success or a failure.
 *
 * @param T The type of the value in the success case.
 */
sealed class ResultOf<out T> {

    /**
     * Represents a successful result of an operation.
     *
     * @param R The type of the value in the success case.
     * @property value The value returned by the successful operation.
     */
    data class Success<out R>(val value: R) : ResultOf<R>()

    /**
     * Represents a failure result of an operation.
     *
     * @property message An optional message describing the failure.
     * @property throwable An optional throwable providing additional details about the failure.
     */
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ) : ResultOf<Nothing>()
}