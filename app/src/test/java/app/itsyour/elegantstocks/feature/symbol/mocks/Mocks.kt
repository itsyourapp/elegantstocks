package app.itsyour.elegantstocks.feature.symbol.mocks

import app.itsyour.elegantstocks.feature.symbol.model.Symbol

const val SYMBOL_APPLE_POSITION = 0
const val SYMBOL_GOOGLE_POSITION = 1

fun fakeSymbols() = listOf(fakeSymbol(), anotherFakeSymbol())

fun fakeSymbol() =
Symbol("AAPL", "Apple", "March 5th", true, "CS", "ID")

fun anotherFakeSymbol() =
        Symbol("GOOG", "Google", "March 5th", true, "CS", "ID")