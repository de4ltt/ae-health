package com.ae.network.handler

import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import retrofit2.Response

suspend fun <T> handleNetworkRequest(
    request: suspend () -> Response<T>
): NetworkRequestResult<T> = try {
    val response = request()

    if (response.body() == null)
        throw Exception("Body is null")

    if (response.isSuccessful)
        NetworkRequestResult.Success(response.body()!!)
    else NetworkRequestResult.Error(
        error = when (response.code()) {
            in 300..399 -> NetworkRequestError.Redirect(response.code().toString())
            in 400..499 -> NetworkRequestError.ClientError(response.code().toString())
            in 500..599 -> NetworkRequestError.ServerError(response.code().toString())
            else -> NetworkRequestError.UnknownError(response.code().toString())
        }
    )
} catch (e: Throwable) {
    NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
}

suspend fun <T> handleNetworkRequests(
    requests: List<Deferred<NetworkRequestResult<List<T>>>>
): NetworkRequestResult<List<T>> {
    return try {
        val responses = requests.awaitAll()

        val firstError = responses.firstOrNull { it is NetworkRequestResult.Error }
        if (firstError != null) {
            return firstError
        }

        val combinedData = responses
            .filterIsInstance<NetworkRequestResult.Success<List<T>>>()
            .flatMap { it.data }

        NetworkRequestResult.Success(combinedData)
    } catch (e: Throwable) {
        NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
    }
}

suspend fun <T, V> handleNetworkRequest(
    request: suspend () -> Response<T>,
    transform: (T) -> V
): NetworkRequestResult<V> = try {
    val response = request()

    if (response.body() == null)
        throw Exception("Body is null")

    if (response.isSuccessful)
        NetworkRequestResult.Success(transform(response.body()!!))
    else NetworkRequestResult.Error(
        error = when (response.code()) {
            in 300..399 -> NetworkRequestError.Redirect(response.code().toString())
            in 400..499 -> NetworkRequestError.ClientError(response.code().toString())
            in 500..599 -> NetworkRequestError.ServerError(response.code().toString())
            else -> NetworkRequestError.UnknownError(response.code().toString())
        }
    )
} catch (e: Throwable) {
    NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
}