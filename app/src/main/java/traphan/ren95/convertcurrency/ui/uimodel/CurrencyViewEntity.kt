package traphan.ren95.convertcurrency.ui.uimodel

data class CurrencyViewEntity(val name: String, val charCode: String, val rate: Float, var currentNominal: Float, var total: Float, var cursorPositionNominal: Int,
                              var cursorPositionTotal: Int, val patchImage: String?, val idRemote: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyViewEntity

        if (name != other.name) return false
        if (charCode != other.charCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + charCode.hashCode()
        return result
    }
}


