package ru.normno.paginationcmp

import io.ktor.client.engine.HttpClientEngine

expect class HttpClientEngineFactory() {
    fun create(): HttpClientEngine
}