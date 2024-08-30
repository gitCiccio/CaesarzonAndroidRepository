package com.example.caesarzonapplication.model.dto.productDTOS

import android.graphics.Bitmap

data class ProductCartWithImage(
    val product: ProductCartDTO,
    val image: Bitmap?
) {
}