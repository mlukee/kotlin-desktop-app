package data.dao

import data.model.Item
import java.util.*

interface ItemsDao {
    fun getById(id: UUID): Item?
    fun getAll(): List<Item>
    fun insert(invoiceID: UUID, itemID: UUID, quantity: Int): Boolean
    fun update(id: UUID): Boolean
    fun delete(id: UUID): Boolean
}