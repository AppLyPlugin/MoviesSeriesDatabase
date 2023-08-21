package com.applyplugin.moviesseriesdatabase.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.BACK_ONLINE
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.DEFAULT_SORT
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.DEFAULT_YEAR
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.PREF_NAME
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.SORT
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.SORT_ID
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.YEAR
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.YEAR_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKey{
        val selectedYear = stringPreferencesKey(YEAR)
        val selectedYearId = intPreferencesKey(YEAR_ID)
        val selectedSort = stringPreferencesKey(SORT)
        val selectedSortId = intPreferencesKey(SORT_ID)
        val backOnline = booleanPreferencesKey(BACK_ONLINE)
    }

    suspend fun saveMoviesSeriesFilter(
        selectedYear: String,
        selectedYearId: Int,
        selectedOrder: String,
        selectedOrderId: Int
    ){

        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.selectedYear] = selectedYear
            preferences[PreferencesKey.selectedYearId] = selectedYearId
            preferences[PreferencesKey.selectedSort] = selectedOrder
            preferences[PreferencesKey.selectedSortId] = selectedOrderId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean){
        context.dataStore.edit { preference ->
            preference[PreferencesKey.backOnline] = backOnline
        }
    }


    val readMoviesSeriesFilter: Flow<MoviesSeriesFilter> = context.dataStore.data
        .catch {exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val selectedYear = preferences[PreferencesKey.selectedYear] ?: DEFAULT_YEAR
            val selectedYearId = preferences[PreferencesKey.selectedYearId] ?: 0
            val selectedOrder = preferences[PreferencesKey.selectedSort] ?: DEFAULT_SORT
            val selectedOrderId = preferences[PreferencesKey.selectedSortId] ?: 0
            MoviesSeriesFilter(
                selectedYear,
                selectedYearId,
                selectedOrder,
                selectedOrderId
            )
        }

    val readBackOnline: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map{preference ->
            val backOnline = preference[PreferencesKey.backOnline] ?: false
            backOnline
        }

}

data class MoviesSeriesFilter(
    val selectedYear: String,
    val selectedYearId: Int,
    val selectedOrder: String,
    val selectedOrderId: Int
)