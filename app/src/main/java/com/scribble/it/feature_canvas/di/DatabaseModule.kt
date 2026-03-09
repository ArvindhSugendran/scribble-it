package com.scribble.it.feature_canvas.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.scribble.it.feature_canvas.data.local.db.dao.ScribbleDao
import com.scribble.it.feature_canvas.data.local.db.database.ScribbleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE canvas_table
            ADD COLUMN autoTitleIndex INTEGER
            """.trimIndent()
        )
    }
}

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
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideScribbleDao(scribbleDb: ScribbleDatabase): ScribbleDao {
        return scribbleDb.scribbleDao
    }
}