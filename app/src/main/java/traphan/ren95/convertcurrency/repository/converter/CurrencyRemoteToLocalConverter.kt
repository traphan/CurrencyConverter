package traphan.ren95.convertcurrency.repository.converter

import com.google.gson.GsonBuilder
import traphan.ren95.convertcurrency.api.entity.CurrencyResponse
import traphan.ren95.convertcurrency.api.entity.CurrencyResponseRoot
import traphan.ren95.convertcurrency.database.entity.CurrencyEntity
import com.google.gson.reflect.TypeToken

class CurrencyRemoteToLocalConverter constructor(private val currencyResponseRoot: CurrencyResponseRoot) {

    private fun convert(currencyResponse: CurrencyResponse): CurrencyEntity {
        return CurrencyEntity(
            currencyResponse.ID,
            currencyResponse.NumCode,
            currencyResponse.CharCode,
            currencyResponse.Nominal,
            currencyResponse.Name,
            currencyResponse.Value,
            currencyResponse.Previous
        )
    }

    fun convertAll(): List<CurrencyEntity>  {
        var currencyEntities: List<CurrencyEntity> = arrayListOf()
        val type = object : TypeToken<Map<String, CurrencyResponse>>() {}.type
        val gson = GsonBuilder().create()
        val mapCurrency: Map<String, CurrencyResponse> = gson.fromJson(gson.toJson(currencyResponseRoot.Valute), type)
        mapCurrency.forEach{(key, value) -> currencyEntities = currencyEntities.plus(convert(value))}
        currencyEntities = currencyEntities.plusElement(
            CurrencyEntity(
                "BASE_CURRENCY_RUB",
                "643",
                "RUB",
                1,
                "Российский рубль",
                1f,
                1f
            )
        )
        return currencyEntities
}
}