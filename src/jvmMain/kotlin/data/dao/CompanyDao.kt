package data.dao

import data.dao.DaoCrud
import data.model.Company

interface CompanyDao : DaoCrud<Company> {
    fun getByName(name: String): Company?
    fun getAllTaxPayers(): List<Company>
}