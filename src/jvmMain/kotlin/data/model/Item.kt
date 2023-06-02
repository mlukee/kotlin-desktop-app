package data.model

import data.util.BarcodeUtil
import java.time.LocalDateTime
import java.util.*

enum class TaxRate(val percentage: Double) {
    RATE_22(0.22),
    RATE_9_5(0.095),
    RATE_5(0.05)
}

open class Item(
    name: String,
    price: Double,
//    quantity: Int,
    tax: TaxRate = TaxRate.RATE_9_5,
    barcode: String = BarcodeUtil.generateRandomBarcode(),
    val id: UUID = UUID.randomUUID(),
    val created: LocalDateTime = LocalDateTime.now(),
    var modified: LocalDateTime = LocalDateTime.now(),

    ) : Searchable {
    init {
        if (!BarcodeUtil.isBarcodeValid(barcode)) throw IllegalArgumentException("$name -> Invalid barcode")
    }

    var name = name
        set(value) {
            field = value
            modified = LocalDateTime.now()
        }
    var price = price
        set(value) {
            field = value
            modified = LocalDateTime.now()
        }
//    var quantity = quantity
//        set(value) {
//            field = value
//            modified = LocalDateTime.now()
//        }
    var tax = tax
        set(value) {
            field = value
            modified = LocalDateTime.now()
        }

    var barcode = barcode
        set(value) {
            field = value
            modified = LocalDateTime.now()
        }

    override fun toString(): String {
        return "$id\t$name\t$price\t${tax.percentage * 100}"
    }

    override fun search(valueToFind: String): Boolean {
        val classToString = "$name$price$tax$id$created$modified$barcode".lowercase()
        val valueToFindLower = valueToFind.lowercase()
        return classToString.contains(valueToFindLower)
    }



}
