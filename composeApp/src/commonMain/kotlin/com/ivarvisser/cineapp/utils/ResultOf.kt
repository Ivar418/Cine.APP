package com.ivarvisser.cineapp.utils


/**
 * Represents the result of an operation that can either be successful or result in a failure.
 *
 * This is a sealed class that has two possible outcomes:
 * - Success: Indicates that the operation completed successfully and contains the resulting value.
 * - Failure: Encapsulates details about the error, including an optional message and a throwable.
 */
sealed class ResultOf<out T> {

    /**
     * Represents a successful result of an operation.
     *
     * @param R The type of the value contained in the success result.
     * @property value The value associated with the success result.
     */
    data class Success<out R>(val value: R) : ResultOf<R>()

    /**
     * Represents a failure result in a process or operation.
     * Typically used to encapsulate an error message and/or an exception
     * that occurred during the execution of a computation or workflow.
     *
     * @property message The error message describing the failure. Can be null if no specific message is provided.
     * @property throwable The throwable or exception that caused the failure. Can be null if no exception is associated.
     */
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ) : ResultOf<Nothing>()
}