package ru.normno.paginationcmp

@Serializable
data class ProductDto(
    val id: Long,
    val title: String,
    val price: Double
)

@Serializable
data class ProductResponseDto(
    val products: List<ProductDto>,
    val total: Long
)
