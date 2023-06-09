package com.jezerm.pokepc.entities

enum class BurnRecipe(val item: Item, val result: Item) {
    GLASS(Item.SAND, Item.GLASS),
    IRON(Item.RAW_IRON, Item.IRON)
}