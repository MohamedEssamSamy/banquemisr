package banquemisr.challenge05.data.mapper

import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.data.entity.NowPlayingMovieEntity
import banquemisr.challenge05.data.entity.PopularMovieEntity
import banquemisr.challenge05.data.entity.UpcomingMovieEntity
import banquemisr.challenge05.data.model.MovieModel

object MoviesMapper {
    fun fromMovieModelToMovie(movieModel: MovieModel): Movie {
        return Movie(
            movieModel.id,
            movieModel.title,
            movieModel.release_date,
            "https://image.tmdb.org/t/p/w500/"+movieModel.poster_path
        )
    }

    // movie movie entry mapper functions
    fun fromMovieEntityToMovie(nowPlayingMovieEntity: NowPlayingMovieEntity): Movie {
        return Movie(
            nowPlayingMovieEntity.id,
            nowPlayingMovieEntity.title,
            nowPlayingMovieEntity.releaseDate,
            nowPlayingMovieEntity.posterImg
        )
    }

    fun fromMovieToMovieEntity(movie: Movie): NowPlayingMovieEntity {
        return NowPlayingMovieEntity(movie.id, movie.title, movie.releaseDate, movie.posterImg)
    }

    // popular movie entity mapper functions
    fun fromPopularMovieEntityToMovie(popularMovieEntity: PopularMovieEntity): Movie {
        return Movie(
            popularMovieEntity.id,
            popularMovieEntity.title,
            popularMovieEntity.releaseDate,
            "https://image.tmdb.org/t/p/w500/"+popularMovieEntity.posterImg
        )
    }

    fun fromMovieToPopularMovieEntity(movie: Movie): PopularMovieEntity {
        return PopularMovieEntity(movie.id, movie.title, movie.releaseDate, movie.posterImg)
    }

    // upcoming movie entity mapper functions
    fun fromUpcomingMovieEntityToMovie(upcomingMovieEntity: UpcomingMovieEntity): Movie {
        return Movie(
            upcomingMovieEntity.id,
            upcomingMovieEntity.title,
            upcomingMovieEntity.releaseDate,
            upcomingMovieEntity.posterImg
        )
    }

    fun fromMovieToUpcomingMovieEntity(movie: Movie): UpcomingMovieEntity {
        return UpcomingMovieEntity(movie.id, movie.title, movie.releaseDate, movie.posterImg)
    }
}