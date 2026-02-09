package com.ivor.openanime.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("first_air_date") val firstAirDate: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("genre_ids") val genreIds: List<Int>
)

@Serializable
data class AnimeDetailsDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("first_air_date") val firstAirDate: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("number_of_seasons") val numberOfSeasons: Int,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int,
    @SerialName("seasons") val seasons: List<SeasonDto>
)

@Serializable
data class SeasonDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("episode_count") val episodeCount: Int,
    @SerialName("air_date") val airDate: String?
)
