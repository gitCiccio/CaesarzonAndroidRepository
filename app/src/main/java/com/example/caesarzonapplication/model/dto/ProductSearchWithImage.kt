package com.example.caesarzonapplication.model.dto

import android.graphics.Bitmap

data class ProductSearchWithImage(
    val product: ProductSearchDTO,
    val image: Bitmap?
)
