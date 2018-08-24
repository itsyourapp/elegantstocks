package app.itsyour.elegantstocks.application.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.itsyour.elegantstocks.feature.navigator.quotes.model.Quote
import app.itsyour.elegantstocks.feature.navigator.quotes.model.QuoteDao

@Database(entities = [Quote::class], version = 1)
@TypeConverters(Converters::class)
abstract class TransientDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}