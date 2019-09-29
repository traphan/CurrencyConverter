package traphan.ren95.convertcurrency.ui.recyclerdragandrop.entity

data class BaseCurrencyHeader(val name:String, var position: Int): BaseCurrencyViewEntity(
    RECYCLER_CARD_ITEM_TYPE.HEADER
)