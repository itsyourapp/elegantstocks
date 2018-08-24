package app.itsyour.elegantstocks.feature.symbol.model

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface SymbolDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(symbol: Symbol)

    @Delete
    fun delete(symbol: Symbol)

    @Query("DELETE FROM symbol WHERE symbol = :symbol")
    fun delete(symbol: String)

    @Query("SELECT * FROM symbol ORDER BY symbol ASC")
    fun fetchAll(): Flowable<List<Symbol>>

    @Query("DELETE FROM symbol")
    fun deleteAll()
}