package data.dao.mysql

import data.dao.InternalItemsDao
import data.model.*
import data.util.DatabaseUtil
import java.nio.ByteBuffer
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.*

class MySqlInternalItems : InternalItemsDao {

    override fun getById(id: UUID): Item? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_ID)
                select.setBytes(1, id.asBytes())
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return mapDataToObject(rs)
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null
    }

    override fun getAll(): List<Item>{
        return emptyList()
    }

    override fun insert(invoiceID: UUID, internalItemID: UUID) : Boolean{
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val insert = it.prepareStatement(SQL_INSERT)
                insert.setBytes(1, invoiceID.asBytes())
                insert.setBytes(2, internalItemID.asBytes())
                insert.setString(3, LocalDateTime.now().toString())
                insert.setString(4, LocalDateTime.now().toString())
                insert.executeUpdate()
                return true
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun update(id: UUID): Boolean {
        return true
    }

    override fun delete(id: UUID): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val delete = it.prepareStatement(SQL_DELETE)
                delete.setBytes(1, id.asBytes())
                return delete.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    private fun mapDataToObject(rs: ResultSet): InternalItem {
        val idByteArray = rs.getBytes("idinternal_item")
        val id = idByteArray.asUuid()
        val name = rs.getString("name")
        val internalID = when (rs.getInt("internalID")) {
            6789 -> InternalID.BANANA
            1234 -> InternalID.KIWI
            2345 -> InternalID.LUBENICA
            6812 -> InternalID.MESANO_MLETO_MESO
            else -> InternalID.BANANA
        }
        val department = when (rs.getInt("department")) {
            211 -> Department.SADJE
            212 -> Department.ZELENJAVA
            213 -> Department.MESO
            else -> Department.SADJE
        }
        val weight = rs.getDouble("weight")
        val tax = when (rs.getDouble("tax")) {
            TaxRate.RATE_9_5.percentage -> TaxRate.RATE_9_5
            TaxRate.RATE_5.percentage -> TaxRate.RATE_5
            TaxRate.RATE_22.percentage -> TaxRate.RATE_22
            else -> TaxRate.RATE_9_5 // default value if none of the above matches
        }
        val created = rs.getTimestamp("created").toLocalDateTime()
        val modified = rs.getTimestamp("modified").toLocalDateTime()
        val price = rs.getDouble("price")
        val barcode = rs.getString("barcode")
        return InternalItem(name, internalID, department, weight, tax, id, created, modified, price, barcode)
    }
    companion object {
        private const val TABLE_NAME = "items_internal"
        private const val SQL_GET_BY_ID = "SELECT internal_item.*" +
                "FROM items_internal" +
                "JOIN internal_item ON items_internal.internal_item_idinternal_item = internal_item.idinternal_item" +
                "WHERE items_internal.id = ?"
        private const val SQL_INSERT =
            "INSERT INTO $TABLE_NAME (invoice_idinvoice, internal_item_idinternal_item, created, modified) VALUES (?, ?, ?, ?)"
        private const val SQL_UPDATE =
            "UPDATE $TABLE_NAME SET name = ?, price = ?, tax = ?, barcode = ?, modified = ? WHERE iditem = ?"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE internal_item_idinternal_item = ?"
    }


    private fun UUID.asBytes(): ByteArray {
        val bb = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(this.mostSignificantBits)
        bb.putLong(this.leastSignificantBits)
        return bb.array()
    }

    private fun ByteArray.asUuid(): UUID {
        val bb = ByteBuffer.wrap(this)
        val firstLong = bb.long
        val secondLong = bb.long
        return UUID(firstLong, secondLong)
    }
}