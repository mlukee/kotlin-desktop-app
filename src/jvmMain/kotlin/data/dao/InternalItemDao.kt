package data.dao

import data.model.Department
import data.model.InternalID
import data.model.InternalItem

interface InternalItemDao : DaoCrud<InternalItem> {
    fun getByDepartment(department: Department): List<InternalItem>
    fun getByInternalID(internalID: InternalID): InternalItem?
}