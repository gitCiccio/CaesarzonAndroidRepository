package com.example.caesarzonapplication.model.dto.productDTOS

import android.graphics.Bitmap

data class ProductSearchWithImage(
    val product: ProductSearchDTO,
    val image: Bitmap?
)
