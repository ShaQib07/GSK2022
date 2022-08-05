package com.shakib.gsk2022.common.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun <T> processUseCase(incoming: T): Flow<Resource<T>> = flow {
    val resource: Resource<T> = try {
        Resource.Success(incoming)
    } catch (e: Exception) {
        Resource.Error(e)
    }
    emit(resource)
}.flowOn(Dispatchers.IO)

fun <T> processUseCaseData(incoming: T): Resource<T> =
    try {
        Resource.Success(incoming)
    } catch (e: Exception) {
        Resource.Error(e)
    }
