package com.mercadolivre.checkoutexample.services

sealed class OperationResult<out T> {

    data class Success<out T>(val value: T): OperationResult<T>()
    data class GenericError(val code: Int? = null, val error: String?): OperationResult<Nothing>()
    object NetworkError: OperationResult<Nothing>()
}