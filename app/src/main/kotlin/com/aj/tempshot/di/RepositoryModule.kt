package com.aj.tempshot.di

import com.aj.tempshot.data.repository.ImageRepository
import com.aj.tempshot.domain.repository.IImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindImageRepository(
        imageRepository: ImageRepository
    ): IImageRepository
}
