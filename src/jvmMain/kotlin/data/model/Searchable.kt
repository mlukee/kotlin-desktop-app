package data.model

interface Searchable {
    fun search(valueToFind: String): Boolean
}