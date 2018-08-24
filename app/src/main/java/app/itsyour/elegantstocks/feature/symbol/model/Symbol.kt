package app.itsyour.elegantstocks.feature.symbol.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents reference data for an underlying market symbol.
 */
@Entity
data class Symbol(
        @PrimaryKey
        val symbol: String,
        val name: String,
        val date: String,
        val isEnabled: Boolean,
        val type: String,
        val iexId: String)

fun List<Symbol>.toApiString()
        = joinToString(",", transform = { it.symbol })
