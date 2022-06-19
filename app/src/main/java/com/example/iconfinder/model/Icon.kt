package com.example.iconfinder.model

data class Icon(
    val icon_id: Int,
    val is_premium: Boolean,
    val prices: List<Price>,
    val raster_sizes: List<RasterSize>,
    val categories: List<Category>,
)