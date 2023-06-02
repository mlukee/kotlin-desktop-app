package data.dao.mysql

import data.dao.InvoiceDao
import data.model.Company
import data.model.Invoice
import data.util.DatabaseUtil
import java.nio.ByteBuffer
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

class MySqlInvoice : InvoiceDao {

    override fun getByDate(date: LocalDateTime) : List<Invoice> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_DATE)
                select.setTimestamp(1, Timestamp.valueOf(date))
                val rs = select.executeQuery()
                val list = mutableListOf<Invoice>()
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

    override fun getByDateRange(from: LocalDateTime, to: LocalDateTime): List<Invoice> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_DATE_RANGE)
                select.setTimestamp(1, Timestamp.valueOf(from))
                select.setTimestamp(2, Timestamp.valueOf(to))
                val rs = select.executeQuery()
                val list = mutableListOf<Invoice>()
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

    override fun getById(id: UUID): Invoice? {
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

    override fun getAll(): List<Invoice> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL)
                val rs = select.executeQuery()
                val list = mutableListOf<Invoice>()
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

    override fun insert(obj: Invoice): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val insert = it.prepareStatement(SQL_INSERT)
                insert.setBytes(1, obj.id.asBytes())
                insert.setString(2, obj.cashier)
                insert.setTimestamp(3, Timestamp.valueOf(obj.date))
                insert.setTimestamp(4, Timestamp.valueOf(obj.created))
                insert.setTimestamp(5, Timestamp.valueOf(obj.modified))
                insert.setBytes(6, obj.issuer.id.asBytes())
                insert.setBytes(7, obj.customer?.id?.asBytes())
                return insert.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun update(obj: Invoice): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val update = it.prepareStatement(SQL_UPDATE)
                update.setString(1, obj.cashier)
                update.setTimestamp(2, Timestamp.valueOf(obj.date))
                update.setTimestamp(3, Timestamp.valueOf(obj.modified))
                update.setBytes(4, obj.issuer.id.asBytes())
                update.setBytes(5, obj.customer?.id?.asBytes())
                update.setBytes(6, obj.id.asBytes())
                return update.executeUpdate() > 0
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun delete(obj: Invoice) : Boolean{
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
        private const val TABLE_NAME = "invoice"
        private const val SQL_GET_BY_DATE_RANGE = "SELECT * FROM $TABLE_NAME WHERE date BETWEEN ? AND ?"
        private const val SQL_GET_BY_DATE = "SELECT * FROM $TABLE_NAME WHERE date = ?"
        private const val SQL_GET_BY_ID = "SELECT * FROM $TABLE_NAME WHERE idinvoice = ? LIMIT 1"
        private const val SQL_GET_ALL = "SELECT * FROM $TABLE_NAME"
        private const val SQL_INSERT = "INSERT INTO $TABLE_NAME (idinvoice, cashier, date, created, modified, issuer_id, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_UPDATE = "UPDATE $TABLE_NAME SET cashier = ?, date = ?, modified = ?, issuer = ?, customer = ? WHERE idinvoice = ?"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE idinvoice = ?"
    }

    private fun mapDataToObject(rs: ResultSet): Invoice {
        val idByteArray = rs.getBytes("idinvoice")
        val id = idByteArray.asUuid()
        val cashier = rs.getString("cashier")
        val date = rs.getTimestamp("date").toLocalDateTime()
        val created = rs.getTimestamp("created").toLocalDateTime()
        val modified = rs.getTimestamp("modified").toLocalDateTime()
        val issuerID = rs.getBytes("issuer_id").asUuid()
        val issuerCompany = MySqlCompany().getById(issuerID)
        val customerID = rs.getBytes("customer_id").asUuid()
        val customerCompany = MySqlCompany().getById(customerID)
        return Invoice(id=id, cashier=cashier, issuer=issuerCompany!!, customer=customerCompany, date=date, created=created, modified=modified)
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