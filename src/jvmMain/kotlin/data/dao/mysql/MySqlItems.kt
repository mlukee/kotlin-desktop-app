package data.dao.mysql

import data.dao.ItemsDao
import data.model.Item
import data.model.TaxRate
import data.util.DatabaseUtil
import java.nio.ByteBuffer
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.*

class MySqlItems : ItemsDao {
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

    override fun getAll(): List<Item> {
        return emptyList()
    }

    override fun insert(invoiceID: UUID, itemID: UUID, quantity: Int): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val insert = it.prepareStatement(SQL_INSERT)
                insert.setBytes(1, itemID.asBytes())
                insert.setBytes(2, invoiceID.asBytes())
                insert.setString(3, LocalDateTime.now().toString())
                insert.setString(4, LocalDateTime.now().toString())
                insert.setInt(5, quantity)
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

    companion object {
        private const val TABLE_NAME = "items"
        private const val SQL_GET_BY_ID = "SELECT item.*" +
                "FROM items" +
                "JOIN item ON items.item_iditem = item.iditem" +
                "WHERE items.id = ?"
        private const val SQL_INSERT =
            "INSERT INTO $TABLE_NAME (item_iditem, invoice_idinvoice, created, modified, quantity) VALUES (?, ?, ?, ?, ?)"
        private const val SQL_UPDATE =
            "UPDATE $TABLE_NAME SET name = ?, price = ?, tax = ?, barcode = ?, modified = ? WHERE iditem = ?"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE item_iditem = ?"
    }

    private fun mapDataToObject(rs: ResultSet): Item {
        val idByteArray = rs.getBytes("iditem")
        val id = idByteArray.asUuid()
        val name = rs.getString("name")
        val price = rs.getDouble("price")
        val taxValue = rs.getDouble("tax")
        val tax = when (taxValue) {
            TaxRate.RATE_9_5.percentage -> TaxRate.RATE_9_5
            TaxRate.RATE_5.percentage -> TaxRate.RATE_5
            TaxRate.RATE_22.percentage -> TaxRate.RATE_22
            else -> TaxRate.RATE_9_5 // default value if none of the above matches
        }
        val barcode = rs.getString("barcode")
        val created = rs.getTimestamp("created").toLocalDateTime()
        val modified = rs.getTimestamp("modified").toLocalDateTime()
        return Item(name, price, tax, barcode, id, created, modified)
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