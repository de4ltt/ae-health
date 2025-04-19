package com.ae.search.jsoup

import com.ae.config.di.annotation.SecretProperty
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.TypedItemResponse
import com.ae.search.model.SearchParamsNetwork
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

internal abstract class IJsoupFindApi(
    @SecretProperty("find_url") private val findUrl: String
) {
    
    suspend fun find(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "$findUrl/?q=${searchParams.query}"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val results: MutableList<TypedItemResponse> = mutableListOf()

            searchParams.itemFilters.forEach { category ->
                val result = category.nearbySearchApi.select(doc)

                if (result is NetworkRequestResult.Success)
                    results.addAll(result.data)
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }
    
    abstract suspend fun findMedicine(query: String): NetworkRequestResult<List<TypedItemResponse>>
}