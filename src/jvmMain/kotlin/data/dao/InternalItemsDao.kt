package data.dao

import data.model.Item
import java.util.*

interface InternalItemsDao {
    fun getById(id: UUID): Item?
    fun getAll(): List<Item>
    fun insert(invoiceID: UUID, internalItemID: UUID): Boolean
    fun update(id: UUID): Boolean
    fun delete(id: UUID): Boolean


//    fun getInternalItemsForInvoice(invoiceId: UUID): List<Item>
//    fun getItemsForInvoice(invoiceId: UUID): List<Item>
//    fun getAllInternalItems(): List<Item>
//    fun getAllItems(): List<Item>

}