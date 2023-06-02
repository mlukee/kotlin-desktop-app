package data.model

import data.util.BarcodeUtil
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.reflect.full.memberProperties

//class Items : LinkedHashMap<UUID, Item>(), Searchable {
//
//    var amount: Double = 0.0
//    private val created: LocalDateTime = LocalDateTime.now()
//    private var modified: LocalDateTime = LocalDateTime.now()
//
//    override fun search(valueToFind: String): Boolean {
//        val classToString = this::class.memberProperties
//            .map { it.getter.call(this).toString() }
//            .joinToString("")
//            .lowercase()
//        val valueToFindLower = valueToFind.lowercase()
//        return classToString.contains(valueToFindLower)
//    }
//
//    override fun put(key: UUID, value: Item): Item? {
//        if (containsKey(key)) {
//            updateTotalAmount(value)
//            get(key)!!.quantity = get(key)!!.quantity + 1
//        } else {
//            updateTotalAmount(value)
//            super.put(key, value)
//        }
//        modified = LocalDateTime.now()
//        return get(key)
//    }
//
//    override fun remove(key: UUID): Item? {
//        if (containsKey(key)) {
//            if (get(key)!!.quantity - 1 > 0) {
//                get(key)!!.quantity = get(key)!!.quantity - 1
//                updateTotalAmount(get(key)!!)
//            } else {
//                val itemToRemove = get(key)!!
//                updateTotalAmount(itemToRemove, remove = true)
//                super.remove(key)
//
//            }
//        } else {
//            throw IllegalStateException("Izdelek ne obstaja v seznamu.")
//        }
//        modified = LocalDateTime.now()
//        return get(key)
//    }
//
//    fun updateItem(itemToUpdate: Item) {
//        if (containsKey(itemToUpdate.id)) {
//            val oldQuantity = get(itemToUpdate.id)!!.quantity
//
//            if (itemToUpdate.quantity <= 0)
//                throw IllegalStateException("Quantity can't be Negative or 0")
//            get(itemToUpdate.id)!!.quantity = itemToUpdate.quantity
//            get(itemToUpdate.id)!!.name = itemToUpdate.name
//            get(itemToUpdate.id)!!.price = itemToUpdate.price
//            get(itemToUpdate.id)!!.tax = itemToUpdate.tax
//            get(itemToUpdate.id)!!.barcode = BarcodeUtil.generateRandomBarcode()
//            val quantityDiff = itemToUpdate.quantity - oldQuantity
//            val tmpItem = Item(
//                itemToUpdate.name,
//                itemToUpdate.price,
//                quantityDiff,
//                itemToUpdate.tax,
//                itemToUpdate.barcode,
//                itemToUpdate.id
//            )
//            updateTotalAmount(tmpItem)
//
//        } else {
//            throw IllegalStateException("Izdelek $itemToUpdate.name ne obstaja v seznamu.")
//        }
//        modified = LocalDateTime.now()
//    }
//
//    fun removeQuantity(q: Int, itemToUpdate: Item) {
//        if (containsKey(itemToUpdate.id)) {
//            if (get(itemToUpdate.id)!!.quantity - q < 0)
//                throw IllegalStateException("Quantity input too big!")
//            if (get(itemToUpdate.id)!!.quantity - q == 0) {
//                super.remove(itemToUpdate.id)
//                updateTotalAmount(get(itemToUpdate.id)!!, remove = true)
//            } else
//                get(itemToUpdate.id)!!.quantity -= q
//            val tmpItem = Item(
//                itemToUpdate.name,
//                itemToUpdate.price,
//                itemToUpdate.quantity,
//                itemToUpdate.tax,
//                itemToUpdate.barcode,
//                itemToUpdate.id
//            )
//            tmpItem.quantity = q
//            updateTotalAmount(tmpItem, remove = true)
//        } else {
//            throw IllegalStateException("Izdelek $itemToUpdate.name ne obstaja v seznamu.")
//        }
//
//        modified = LocalDateTime.now()
//    }
//
//    private fun updateTotalAmount(item: Item, remove: Boolean = false) {
//        val itemPriceWithTax = item.price * (1 + item.tax.percentage)
//        val quantity = if (remove) -item.quantity else item.quantity
//        amount += itemPriceWithTax * quantity
//    }
//
//    fun print() {
//        for (item in this.values) {
//            println(item.toString())
//        }
//    }
//}
