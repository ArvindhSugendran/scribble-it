package com.scribble.it.feature_canvas.di

import android.content.Context
import androidx.room.Room
import com.scribble.it.feature_canvas.data.local.db.dao.ScribbleDao
import com.scribble.it.feature_canvas.data.local.db.database.ScribbleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideScribbleDatabase(@ApplicationContext context: Context): ScribbleDatabase {
        return Room.databaseBuilder(
            context = context,
            ScribbleDatabase::class.java,
            ScribbleDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideScribbleDao(scribbleDb: ScribbleDatabase): ScribbleDao {
        return scribbleDb.scribbleDao
    }
}