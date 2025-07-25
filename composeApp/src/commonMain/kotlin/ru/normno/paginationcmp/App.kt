package ru.normno.paginationcmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = viewModel(
            initializer = {
                ProductsViewModel(
                    ProductsApi(
                        client = HttpClient(
                            engine = HttpClientEngineFactory().create()
                        ) {
                            install(Logging) {
                                logger = Logger.SIMPLE
                                level = LogLevel.ALL
                            }
                            install(ContentNegotiation) {
                                json(
                                    json = Json {
                                        ignoreUnknownKeys = true
                                    }
                                )
                            }
                        }
                    )
                )

            }
        )

            val state by viewModel.state.collectAsStateWithLifecycle()

        val lazyListState = rememberLazyListState()

        LaunchedEffect(state.products) {
            snapshotFlow {
                lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            }
                .distinctUntilChanged()
                .collect { lastVisibleIndex ->
                    if (lastVisibleIndex == state.products.lastIndex) {
                        viewModel.loadNextItems()
                    }
                }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
        ) { contentPadding ->
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = contentPadding,
            ) {
                items(
                    items = state.products,
                    key = { product ->
                        product.id
                    },
                ) { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                            ),
                    ) {
                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = product.price.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                item {
                    if (state.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}