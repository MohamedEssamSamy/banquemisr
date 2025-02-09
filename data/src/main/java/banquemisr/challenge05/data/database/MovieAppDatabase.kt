package banquemisr.challenge05.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import banquemisr.challenge05.data.entity.MovieDetailsEntity
//import banquemisr.challenge05.data.entity.MovieDetailsEntity
import banquemisr.challenge05.data.entity.NowPlayingMovieEntity
import banquemisr.challenge05.data.entity.PopularMovieEntity
import banquemisr.challenge05.data.entity.UpcomingMovieEntity

@Database(entities = arrayOf(NowPlayingMovieEntity::class,PopularMovieEntity::class,UpcomingMovieEntity::class, MovieDetailsEntity::class), version = 1)
@TypeConverters(Converters::class)
abstract class MovieAppDatabase:RoomDatabase() {
    abstract fun getNowPlayingMoviesDao():NowPlayingMoviesDao
    abstract fun getPopularMoviesDao():PopularMoviesDao
    abstract fun getUpcomingMoviesDao():UpcomingMoviesDao
    abstract fun getMovieDetailsDao():MovieDetailsDao
}