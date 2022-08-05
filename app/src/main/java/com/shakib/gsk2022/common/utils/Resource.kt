package com.shakib.gsk2022.common.utils

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Loading<T>(val data: T? = null) : Resource<T>()
    data class Error<T>(val throwable: Throwable) : Resource<T>()
}
