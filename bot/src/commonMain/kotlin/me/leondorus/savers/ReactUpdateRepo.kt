package me.leondorus.savers

import kotlinx.coroutines.flow.Flow
import me.leondorus.data.React

interface ReactUpdateRepo {
    fun getReactUpdates(): Flow<ReactUpdate>
}

data class ReactUpdate(
    val oldReacts: List<React>,
    val newReacts: List<React>
)