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

    fun validateCreditCardDetails(cardHolderName: String, cardNumber: String, expirationDate: String, cvc: String): Boolean {

        val cardHolderNameRegex = Regex("^(?=.{5,40}\$)[a-zA-Z]+( [a-zA-Z]+){0,3}")
        if(!cardHolderName.matches(cardHolderNameRegex)){
            return false
        }

        val cardNumberRegex = Regex("\\d{4} \\d{4} \\d{4} \\d{4}")
        if (!cardNumber.matches(cardNumberRegex)) {
            return false
        }

        val expirationRegex = Regex("([0-9]+)-([0-9]+)")
        if (!expirationDate.matches(expirationRegex)) {
            return false
        }

        if (cvc.length !in 3..4 || !cvc.all { it.isDigit() }) {
            return false
        }

        return true
    }
}
