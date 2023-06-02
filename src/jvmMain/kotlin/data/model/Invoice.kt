package data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class Invoice(
    val issuer: Company,
    val cashier: String = "demo",
    var customer: Company? = null,
    val date: LocalDateTime = LocalDateTime.now(),
    val id: UUID = UUID.randomUUID(),
    val created: LocalDateTime = LocalDateTime.now(),
    var modified: LocalDateTime = LocalDateTime.now(),
) : Searchable {


    override fun search(valueToFind: String): Boolean {
        val classToString =
            "${issuer.toString()}$cashier${customer?.toString()}$date$id$created$modified$".lowercase()
        val valueToFindLower = valueToFind.lowercase()
        return classToString.contains(valueToFindLower)
    }

    fun addItem(newItem: Item) {
//        items.put(newItem.id, newItem)
        modified = LocalDateTime.now()
    }

    fun addCompany(c: Company) {
        customer = c
    }

    fun removeItem(itemToRemove: Item) {
        try {
//            items.remove(itemToRemove.id)
        } catch (e: IllegalStateException) {
            println("${itemToRemove.name}: $e")
        }
        modified = LocalDateTime.now()
    }

    fun updateItem(itemToUpdate: Item) {
        try {
//            items.updateItem(itemToUpdate)
        } catch (e: IllegalStateException) {
            println("${itemToUpdate.name}: $e")
        }
    }

    fun removeQuantity(q: Int, itemToUpdate: Item) {
        try {
//            items.removeQuantity(q, itemToUpdate)
        } catch (e: IllegalStateException) {
            println("${itemToUpdate.name}: $e")
        }

    }

    fun print() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val formattedDateTime = date.format(formatter)
        println(issuer.toString())
        println("----------------------------")
        println("Racun: $id")
        println("Datum: $formattedDateTime")
        println("----------------------------")
        if (customer != null && customer?.taxpayer == true) {
            println("Kupec: ")
            println(customer.toString())
            println("----------------------------")
        }

        println("Kol.\tIme\tCena\tDDV")
//        items.print()
        println("----------------------------")
//        val znesek = String.format("%.2f", items.amount)
//        println("Skupaj EUR\t $znesek")
        println("Račun izdal: $cashier")
    }

}
