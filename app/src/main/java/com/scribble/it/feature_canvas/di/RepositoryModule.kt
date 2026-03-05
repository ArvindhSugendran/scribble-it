package com.scribble.it.feature_canvas.di

import com.scribble.it.feature_canvas.data.fileManager.FileManagerImpl
import com.scribble.it.feature_canvas.data.repository.CanvasRepositoryImpl
import com.scribble.it.feature_canvas.domain.fileManager.FileManager
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCanvasRepository(canvasRepositoryImpl: CanvasRepositoryImpl): CanvasRepository

    @Binds
    @Singleton
    abstract fun bindFileManager(fileManagerImpl: FileManagerImpl): FileManager
}