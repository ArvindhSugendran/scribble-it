package com.scribble.it.feature_canvas.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.scribble.it.feature_canvas.core.SCRIBBLE_ONBOARDING
import com.scribble.it.feature_canvas.core.SCRIBBLE_SORT_MODE
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
        private const val DEFAULT_SORT_MODE = "CREATED_DATE_DESC"
        val SCRIBBLE_VIEW_MODE_KEY = stringPreferencesKey(SCRIBBLE_VIEW_MODE)
        val SCRIBBLE_SORT_MODE_KEY = stringPreferencesKey(SCRIBBLE_SORT_MODE)
        val SCRIBBLE_ONBOARDING_KEY= booleanPreferencesKey(SCRIBBLE_ONBOARDING)
    }

    suspend fun setScribbleViewMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[SCRIBBLE_VIEW_MODE_KEY] = mode
        }
    }

    fun getScribbleViewMode(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[SCRIBBLE_VIEW_MODE_KEY] ?: DEFAULT_VIEW_MODE
        }.distinctUntilChanged()
    }

    suspend fun setScribbleSortMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[SCRIBBLE_SORT_MODE_KEY] = mode
        }
    }

    fun getScribbleSortMode(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[SCRIBBLE_SORT_MODE_KEY] ?: DEFAULT_SORT_MODE
        }.distinctUntilChanged()
    }

    suspend fun setOnBoardingCompleted() {
        dataStore.edit { preferences ->
            preferences[SCRIBBLE_ONBOARDING_KEY] = true
        }
    }

    fun getOnBoardingStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SCRIBBLE_ONBOARDING_KEY] ?: false
        }.distinctUntilChanged()
    }

}