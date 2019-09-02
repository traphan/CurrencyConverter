package com.traphan.currencyconverter.ui.recyclerhelper

interface ItemTouchHelperViewHolder {

    /**
     * Called when the [ItemTouchHelper] first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    abstract fun onItemSelected()


    /**
     * Called when the [ItemTouchHelper] has completed the move or swipe, and the active item
     * state should be cleared.
     */
    abstract fun onItemClear()
}