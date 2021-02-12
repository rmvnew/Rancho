package com.example.rancho.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first

class DataStoreUtil(context: Context) {

    val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "settings"
    )


    suspend fun saveBoolean(key:String,value:Boolean){
        val prefKey = booleanPreferencesKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    suspend fun readBoolean(key:String):Boolean?{
        val prefKey = booleanPreferencesKey(key)
        val prefs = dataStore.data.first()
        return prefs[prefKey]
    }



}