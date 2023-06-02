package data.model

import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.full.memberProperties

class Company(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    val taxNumber: String,
    val registrationNumber: String,
    var taxpayer: Boolean = false,
    var address: String = "",
    val created: LocalDateTime = LocalDateTime.now(),
    var modified: LocalDateTime = LocalDateTime.now(),
) :
    Searchable {

    override fun search(valueToFind: String): Boolean {
        val classToString = this::class.memberProperties
            .map { it.getter.call(this).toString() }
            .joinToString("")
            .lowercase()
        val valueToFindLower = valueToFind.lowercase()
        return classToString.contains(valueToFindLower)
    }

    override fun toString(): String {
        return ("$name${if (address != "") '\n' + address + '\n' else '\n'}ID št. za DDV: SI$taxNumber")
    }
}