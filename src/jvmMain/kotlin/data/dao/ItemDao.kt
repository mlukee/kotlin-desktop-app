package data.dao

import data.model.Item

interface ItemDao: DaoCrud<Item> {
    fun getByBarcode(barcode: String): Item?
    fun getByName(name: String): Item?
}