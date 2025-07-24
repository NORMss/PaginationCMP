package ru.normno.paginationcmp

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin


actual class HttpClientEngineFactory {
    actual fun create(): HttpClientEngine {
        return Darwin.create()
    }
}