package data.util

import java.util.*
import kotlin.random.Random

object BarcodeUtil {
    fun isBarcodeValid(barcode: String): Boolean {
        if (barcode.length != 13) return false

        if (!barcode.matches("[0-9]+".toRegex())) return false

        val checkDigit = calculateCheckDigit(barcode)
        return checkDigit == barcode[12].toString()
    }

    fun calculateCheckDigit(barcode: String): String {
        val digitSum = barcode.take(12).map { it.toString().toInt() }
            .mapIndexed { index, digit -> if (index % 2 == 0) digit else digit * 3 }
            .sum()
        val checkDigit = (10 - (digitSum % 10)) % 10
        return checkDigit.toString()
    }

    fun generateBarcode(internalID: Int, department: Int, price: Double): String {
        var barcode = internalID.toString() + department.toString()
        var priceToString = price.toString().replace(".", "")
        if (priceToString.length < 5) {
            priceToString = priceToString.padStart(5, '0')
        }
        barcode += priceToString
        barcode += calculateCheckDigit(barcode)
        return "$barcode"
    }

    fun generateRandombarcode(): String {
        val randomNumber = StringBuilder()
        repeat(12) {
            randomNumber.append(Random.nextInt(10))
        }
        return randomNumber.toString()
    }
    fun generateRandomBarcode(): String {
        var barcode = generateRandombarcode()
        barcode += calculateCheckDigit(barcode)
        return barcode
    }

}