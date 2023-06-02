package data.model

import data.util.BarcodeUtil
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToInt

enum class InternalID(val value: Int, val pricePerKG: Double) {
    BANANA(6789, 1.5),
    KIWI(1234, 1.8),
    LUBENICA(2345, 2.0),
    MESANO_MLETO_MESO(6812, 5.0),
}

enum class Department(val value: Int) {
    SADJE(211),
    ZELENJAVA(212),
    MESO(213)
}

class InternalItem(
    name: String,
    val internalID: InternalID,
    val department: Department,
    var weight: Double = 0.0,
    tax: TaxRate = TaxRate.RATE_9_5,
    id: UUID = UUID.randomUUID(),
    created: LocalDateTime = LocalDateTime.now(),
    modified: LocalDateTime = LocalDateTime.now(),
    var totalPrice: Double = if (weight != 0.0) (weight * internalID.pricePerKG) * (1 + tax.percentage) else (internalID.pricePerKG * (1 + tax.percentage)) * 1,
    barcode: String = BarcodeUtil.generateBarcode(
        internalID.value,
        department.value,
        (totalPrice * 100.0).roundToInt() / 100.0
    )
) : Item(
    name = name,
    price = totalPrice,
//    quantity = 1,
    barcode = barcode,
    tax = tax,
    id = id,
    created = created,
    modified = modified,
) {

    override fun toString(): String {
        return "1\t$name\t${String.format("%.2f", totalPrice)}\t${tax.percentage * 100}%\t${weight}kg"
    }
}
