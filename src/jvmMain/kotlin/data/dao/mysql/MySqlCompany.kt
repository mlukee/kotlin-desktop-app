package data.dao.mysql

import data.dao.CompanyDao
import data.model.Company
import data.util.DatabaseUtil
import java.nio.ByteBuffer
import java.sql.ResultSet
import java.util.*

class MySqlCompany : CompanyDao {
    override fun getByName(name: String) : Company? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_NAME)
                select.setString(1, name)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return mapDataToObject(rs)
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
        return null
    }

    override fun getAllTaxPayers() : List<Company>{
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL_TAX_PAYERS)
                select.setBoolean(1, true)
                val rs = select.executeQuery()
                val list = mutableListOf<Company>()
                while (rs.next()) {
                    list.add(mapDataToObject(rs))
                }
                return list
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
        return emptyList()
    }

    override fun getById(id: UUID) : Company? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_ID)
                select.setBytes(1, id.asBytes())
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return mapDataToObject(rs)
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
        return null
    }

    override fun getAll() : List<Company> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL)
                val rs = select.executeQuery()
                val list = mutableListOf<Company>()
                while (rs.next()) {
                    list.add(mapDataToObject(rs))
                }
                return list
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
        return emptyList()
    }

    override fun insert(obj: Company) : Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val insert = it.prepareStatement(SQL_INSERT)
                insert.setBytes(1, obj.id.asBytes())
                insert.setString(2, obj.name)
                insert.setString(3, obj.taxNumber)
                insert.setString(4, obj.registrationNumber)
                insert.setBoolean(5, obj.taxpayer)
                insert.setString(6, obj.address)
                insert.setString(7, obj.created.toString())
                insert.setString(8, obj.modified.toString())
                return insert.executeUpdate() > 0
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
        return false
    }

    override fun update(obj: Company) : Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val update = it.prepareStatement(SQL_UPDATE)
                update.setString(1, obj.name)
                update.setString(2, obj.taxNumber)
                update.setString(3, obj.registrationNumber)
                update.setBoolean(4, obj.taxpayer)
                update.setString(5, obj.address)
                update.setString(6, obj.modified.toString())
                update.setBytes(7, obj.id.asBytes())
                return update.executeUpdate() > 0
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
        return false
    }

    override fun delete(obj: Company) : Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val delete = it.prepareStatement(SQL_DELETE)
                delete.setBytes(1, obj.id.asBytes())
                return delete.executeUpdate() > 0
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
        return false
    }

    companion object {
        private const val TABLE_NAME = "company"
        private const val SQL_GET_BY_NAME = "SELECT * FROM $TABLE_NAME WHERE name = ? LIMIT 1"
        private const val SQL_GET_ALL_TAX_PAYERS = "SELECT * FROM $TABLE_NAME WHERE taxPayer = ?"
        private const val SQL_GET_BY_ID = "SELECT * FROM $TABLE_NAME WHERE idcompany = ? LIMIT 1"
        private const val SQL_GET_ALL = "SELECT * FROM $TABLE_NAME"
        private const val SQL_INSERT = "INSERT INTO $TABLE_NAME (idcompany, name, taxNumber, registrationNumber, taxPayer, address, created, modified) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_UPDATE = "UPDATE $TABLE_NAME SET name = ?, taxNumber = ?, registrationNumber = ?, taxPayer = ?, address = ?, modified = ? WHERE idcompany = ?"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE idcompany = ?"
    }

    private fun mapDataToObject(rs: ResultSet): Company {
        val idByteArray = rs.getBytes("idcompany")
        val id = idByteArray.asUuid()
        val name = rs.getString("name")
        val taxNumber = rs.getString("taxNumber")
        val registrationNumber = rs.getString("registrationNumber")
        val taxPayer = rs.getBoolean("taxPayer")
        val address = rs.getString("address")
        val created = rs.getTimestamp("created").toLocalDateTime()
        val modified = rs.getTimestamp("modified").toLocalDateTime()
        return Company(id, name, taxNumber, registrationNumber, taxPayer, address, created, modified)
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