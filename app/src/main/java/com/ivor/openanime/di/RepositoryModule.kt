package com.ivor.openanime.di

import com.ivor.openanime.data.repository.AnimeRepositoryImpl
import com.ivor.openanime.domain.repository.AnimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAnimeRepository(
        animeRepositoryImpl: AnimeRepositoryImpl
    ): AnimeRepository
}
