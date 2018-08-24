package app.itsyour.elegantstocks.feature.navigator.quotes.model

import androidx.room.*
import io.reactivex.Observable

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quote: Quote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(quotes: List<Quote>)

    @Delete
    fun delete(quote: Quote)

    @Query("SELECT * FROM quote ORDER BY symbol ASC")
    fun fetchAll(): Observable<List<Quote>>

    @Query("DELETE FROM quote")
    fun deleteAll()
}