package ru.normno.paginationcmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductsList(
    val products: List<ProductDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class ProductsViewModel(
    private val productsApi: ProductsApi,
) : ViewModel() {
    private val _state = MutableStateFlow(ProductsList())
    val state = _state.asStateFlow()

    private val pageSize = 10
    private val paginator = Paginator<Int, ProductResponseDto>(
        initialKey = 0,
        onLoadUpdated = { isLoading ->
            _state.update {
                it.copy(
                    isLoading = isLoading,
                )
            }
        },
        onRequest = { page ->
            productsApi.getProducts(
                pageSize = pageSize,
                page = page,
            )
        },
        getNextKey = { currentPage, _ ->
            currentPage + 1
        },
        onError = { error ->
            _state.update {
                it.copy(
                    error = error?.message,
                )
            }
        },
        onSuccess = { result, newKey ->
            _state.update {
                it.copy(
                    products = it.products + result.products,
                    error = null,
                )
            }
        },
        endReached = { currentKey, result ->
            (currentKey * pageSize) >= result.total
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}