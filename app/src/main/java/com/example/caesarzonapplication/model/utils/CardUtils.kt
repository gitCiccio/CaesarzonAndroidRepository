package com.example.caesarzonapplication.model.utils

class CardUtils {

    fun maskCreditCard(cardNumber: String): String {
        val lastFourDigits = cardNumber.takeLast(4)
        val maskedString = buildString {
            repeat(cardNumber.length - 4) {
                append('*')
            }
            append(" $lastFourDigits")
        }
        return maskedString
    }

    fun validateCreditCardDetails(cardNumber: String, expirationDate: String, cvc: String): Boolean {
        // Check if the card number contains 16 digits
        if (cardNumber.length != 16 || !cardNumber.all { it.isDigit() }) {
            return false
        }

        // Check if the expiration date is valid and in the format MM/YY
        val expirationRegex = Regex("^(0[1-9]|1[0-2])/[0-9]{2}$")
        if (!expirationDate.matches(expirationRegex)) {
            return false
        }

        // Check if the CVC contains 3 or 4 digits
        if (cvc.length !in 3..4 || !cvc.all { it.isDigit() }) {
            return false
        }

        return true
    }
}
