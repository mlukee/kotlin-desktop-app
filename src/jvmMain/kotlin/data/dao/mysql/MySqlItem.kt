package data.dao.mysql

import data.dao.ItemDao
import data.model.Item
import data.model.TaxRate
import data.util.DatabaseUtil
import java.nio.ByteBuffer
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.*

class MySqlItem : ItemDao {
    override fun getByBarcode(barcode: String): Item? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_BARCODE)
                select.setString(1, barcode)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return mapDataToObject(rs)
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null
    }

    override fun getByName(name: String): Item? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_NAME)
                select.setString(1, name)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return mapDataToObject(rs)
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null
    }

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
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL)
                val rs = select.executeQuery()
                val items = mutableListOf<Item>()
                while (rs.next()) {
                    items.add(mapDataToObject(rs))
                }
                return items
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return emptyList()
    }

    override fun insert(obj: Item): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val insert = it.prepareStatement(SQL_INSERT)
                insert.setBytes(1, obj.id.asBytes())
                insert.setString(2, obj.name)
                insert.setDouble(3, obj.price)
                insert.setDouble(4, obj.tax.percentage)
                insert.setString(5, obj.barcode)
                insert.setString(6, obj.created.toString())
                insert.setString(7, obj.modified.toString())
                return insert.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun update(obj: Item): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val update = it.prepareStatement(SQL_UPDATE)
                update.setString(1, obj.name)
                update.setDouble(2, obj.price)
                update.setDouble(3, obj.tax.percentage)
                update.setString(4, obj.barcode)
                update.setString(5, LocalDateTime.now().toString())
                update.setBytes(6, obj.id.asBytes())
                return update.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun delete(obj: Item): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val delete = it.prepareStatement(SQL_DELETE)
                delete.setBytes(1, obj.id.asBytes())
                return delete.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    companion object {
        private const val TABLE_NAME = "item"
        private const val SQL_GET_BY_BARCODE = "SELECT * FROM $TABLE_NAME WHERE barcode = ? LIMIT 1"
        private const val SQL_GET_BY_NAME = "SELECT * FROM $TABLE_NAME WHERE name = ? LIMIT 1"
        private const val SQL_GET_BY_ID = "SELECT * FROM $TABLE_NAME WHERE iditem = ? LIMIT 1"
        private const val SQL_GET_ALL = "SELECT * FROM $TABLE_NAME"
        private const val SQL_INSERT =
            "INSERT INTO $TABLE_NAME (iditem, name, price, tax, barcode, created, modified) VALUES (?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_UPDATE =
            "UPDATE $TABLE_NAME SET name = ?, price = ?, tax = ?, barcode = ?, modified = ? WHERE iditem = ?"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE iditem = ?"
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

