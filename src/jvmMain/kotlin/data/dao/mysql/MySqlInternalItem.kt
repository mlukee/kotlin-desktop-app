package data.dao.mysql

import data.dao.InternalItemDao
import data.model.*
import data.util.DatabaseUtil
import java.nio.ByteBuffer
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.*

class MySqlInternalItem : InternalItemDao {

    override fun getByDepartment(department: Department): List<InternalItem> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_DEPARTMENT)
                select.setInt(1, department.value)
                val rs = select.executeQuery()
                val list = mutableListOf<InternalItem>()
                while (rs.next()) {
                    list.add(mapDataToObject(rs))
                }
                return list
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return emptyList()
    }

    override fun getByInternalID(internalID: InternalID): InternalItem? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_INTERNAL_ID)
                select.setInt(1, internalID.value)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return mapDataToObject(rs)
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null
    }

    override fun getById(id: UUID): InternalItem? {
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

    override fun getAll(): List<InternalItem> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL)
                val rs = select.executeQuery()
                val list = mutableListOf<InternalItem>()
                while (rs.next()) {
                    list.add(mapDataToObject(rs))
                }
                return list
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return emptyList()
    }

    override fun insert(obj: InternalItem): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val insert = it.prepareStatement(SQL_INSERT)
                insert.setBytes(1, obj.id.asBytes())
                insert.setString(2, obj.name)
                insert.setInt(3, obj.internalID.value)
                insert.setInt(4, obj.department.value)
                insert.setDouble(5, obj.price)
                insert.setDouble(6, obj.weight)
                insert.setDouble(7, obj.tax.percentage)
                insert.setString(8, obj.barcode)
                insert.setString(9, obj.created.toString())
                insert.setString(10, obj.modified.toString())
                return insert.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun update(obj: InternalItem): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val update = it.prepareStatement(SQL_UPDATE)
                update.setString(1, obj.name)
                update.setDouble(2, obj.price)
                update.setString(3, LocalDateTime.now().toString())
                update.setBytes(4, obj.id.asBytes())
                return update.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun delete(obj: InternalItem): Boolean {
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
        private const val TABLE_NAME = "internal_item"
        private const val SQL_GET_BY_DEPARTMENT = "SELECT * FROM $TABLE_NAME WHERE department = ?"
        private const val SQL_GET_BY_INTERNAL_ID = "SELECT * FROM $TABLE_NAME WHERE internalID = ? "
        private const val SQL_GET_BY_ID = "SELECT * FROM $TABLE_NAME WHERE idinternal_item = ? LIMIT 1"
        private const val SQL_GET_ALL = "SELECT * FROM $TABLE_NAME"
        private const val SQL_INSERT =
            "INSERT INTO $TABLE_NAME (idinternal_item, name, internalID, department, price, weight, tax, barcode, created, modified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_UPDATE =
            "UPDATE $TABLE_NAME SET name = ?, price = ?, modified = ? WHERE idinternal_item = ?"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE idinternal_item = ?"
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