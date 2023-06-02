package data.dao

import data.model.Invoice
import java.time.LocalDateTime

interface InvoiceDao : DaoCrud<Invoice> {
    fun getByDate(dateTime: LocalDateTime): List<Invoice>?
    fun getByDateRange(from: LocalDateTime, to: LocalDateTime): List<Invoice>
}