package app.itsyour.elegantstocks.application.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import app.itsyour.elegantstocks.feature.symbol.model.Symbol
import app.itsyour.elegantstocks.feature.symbol.model.SymbolDao

@Database(entities = [Symbol::class], version = 1)
abstract class PermanentDatabase : RoomDatabase() {
    abstract fun symbolDao(): SymbolDao
}