package banquemisr.challenge05.data.mapper

import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.data.entity.MovieDetailsEntity
import banquemisr.challenge05.data.model.MovieDetailsResponse

object MovieDetailsMapper {
    fun toMovieDetails(movieDetailsResponse: MovieDetailsResponse): MovieDetails {
        return MovieDetails(
            movieDetailsResponse.id,
            movieDetailsResponse.title,
            movieDetailsResponse.release_date,
            "https://image.tmdb.org/t/p/w500/"+movieDetailsResponse.poster_path,
            movieDetailsResponse.genres,
            movieDetailsResponse.overview,
            movieDetailsResponse.runtime
        )
    }

    fun fromMovieDetailsEntityToMovieDetails(movieDetailsEntity: MovieDetailsEntity): MovieDetails {
        return MovieDetails(
            movieDetailsEntity.id,
            movieDetailsEntity.title,
            movieDetailsEntity.releaseDate,
            movieDetailsEntity.posterImg,
            movieDetailsEntity.genres,
            movieDetailsEntity.overview,
            movieDetailsEntity.runtime
        )
    }

    fun fromMovieDetailsToMovieDetailsEntity(movieDetails: MovieDetails): MovieDetailsEntity {
        return MovieDetailsEntity(
            movieDetails.id,
            movieDetails.title,
            movieDetails.releaseDate,
            movieDetails.posterImg,
            movieDetails.genres,
            movieDetails.overview,
            movieDetails.runtime)
    }
}