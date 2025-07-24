package ru.normno.paginationcmp

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual class HttpClientEngineFactory {
    actual fun create(): HttpClientEngine {
        return OkHttp.create()
    }
}