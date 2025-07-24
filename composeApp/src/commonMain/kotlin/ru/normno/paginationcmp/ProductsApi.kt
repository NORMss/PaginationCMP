package ru.normno.paginationcmp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.cio.Response
import io.ktor.http.contentType
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class ProductsApi(
    private val client: HttpClient,
) {
    suspend fun getProducts(
        pageSize: Int = 10,
        page: Int = 1,
    ): Result<ProductResponseDto> {
        val body = try {
            client.get(
                urlString = "https://dummyjson.com/products"
            ) {
                contentType(ContentType.Application.Json)
                parameter("limit", pageSize)
                parameter("skip", pageSize * page)
            }.let { response ->
                response.body<ProductResponseDto>()
            }
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            return Result.failure(e)
        }

        return Result.success(body)
    }
}