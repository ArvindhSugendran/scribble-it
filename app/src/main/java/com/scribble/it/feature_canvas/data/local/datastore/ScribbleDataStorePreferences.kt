package com.scribble.it.feature_canvas.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.scribble.it.feature_canvas.core.SCRIBBLE_VIEW_MODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScribbleDataStorePreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private const val DEFAULT_VIEW_MODE = "GRID"
        val SCRIBBLE_VIEW_MODE_KEY = stringPreferencesKey(SCRIBBLE_VIEW_MODE)
    }

    suspend fun setCanvasViewMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[SCRIBBLE_VIEW_MODE_KEY] = mode
        }
    }

    fun getCanvasViewMode(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[SCRIBBLE_VIEW_MODE_KEY] ?: DEFAULT_VIEW_MODE
        }.distinctUntilChanged()
    }

}