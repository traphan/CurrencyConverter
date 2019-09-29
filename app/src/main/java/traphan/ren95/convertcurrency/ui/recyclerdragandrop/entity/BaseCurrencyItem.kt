package traphan.ren95.convertcurrency.ui.recyclerdragandrop.entity

data class BaseCurrencyItem(val name: String, val patch: String, val idCurrency: String): BaseCurrencyViewEntity(
    RECYCLER_CARD_ITEM_TYPE.ITEM
)