package com.ivor.openanime.data.repository

import com.ivor.openanime.data.remote.TmdbApi
import com.ivor.openanime.data.remote.model.AnimeDetailsDto
import com.ivor.openanime.data.remote.model.AnimeDto
import com.ivor.openanime.data.remote.model.SeasonDetailsDto
import com.ivor.openanime.domain.repository.AnimeRepository
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val api: TmdbApi
) : AnimeRepository {

    override suspend fun getPopularAnime(page: Int): Result<List<AnimeDto>> = runCatching {
        api.getPopularAnime(page = page).results
    }

    override suspend fun searchAnime(query: String, page: Int, filter: String): Result<List<AnimeDto>> = runCatching {
        when (filter) {
            "movie" -> api.searchMovie(query, page).results.map { it.copy(mediaType = "movie") }
            "tv" -> api.searchTv(query, page).results.map { it.copy(mediaType = "tv") }
            else -> api.searchMulti(query, page).results
                .filter { it.mediaType == "tv" || it.mediaType == "movie" }
        }
    }

    override suspend fun getAnimeDetails(id: Int): Result<AnimeDetailsDto> = runCatching {
        api.getAnimeDetails(id = id)
    }

    override suspend fun getMovieDetails(id: Int): Result<AnimeDetailsDto> = runCatching {
        api.getMovieDetails(id = id)
    }

    override suspend fun getMediaDetails(id: Int, mediaType: String): Result<AnimeDetailsDto> = runCatching {
        if (mediaType == "movie") {
            api.getMovieDetails(id = id)
        } else {
            api.getAnimeDetails(id = id)
        }
    }

    override suspend fun getSeasonDetails(animeId: Int, seasonNumber: Int): Result<SeasonDetailsDto> = runCatching {
        api.getSeasonDetails(id = animeId, seasonNumber = seasonNumber)
    }
}
