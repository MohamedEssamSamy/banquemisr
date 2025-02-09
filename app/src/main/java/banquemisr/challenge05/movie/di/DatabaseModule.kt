package banquemisr.challenge05.movie.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import banquemisr.challenge05.data.database.MovieAppDatabase
import banquemisr.challenge05.data.database.MovieDetailsDao
import banquemisr.challenge05.data.database.NowPlayingMoviesDao
import banquemisr.challenge05.data.database.PopularMoviesDao
import banquemisr.challenge05.data.database.UpcomingMoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): MovieAppDatabase {
        return Room.databaseBuilder(context, MovieAppDatabase::class.java, "MovieAppDatabase")
            .build()
    }

    @Provides
    fun provideNowPlayingMoviesDao(movieAppDatabase: MovieAppDatabase): NowPlayingMoviesDao {
        return movieAppDatabase.getNowPlayingMoviesDao()
    }

    @Provides
    fun providePopularMoviesDao(movieAppDatabase: MovieAppDatabase): PopularMoviesDao {
        return movieAppDatabase.getPopularMoviesDao()
    }

    @Provides
    fun provideUpcomingMoviesDao(movieAppDatabase: MovieAppDatabase): UpcomingMoviesDao {
        return movieAppDatabase.getUpcomingMoviesDao()
    }

    @Provides
    fun provideMovieDetailsDao(movieAppDatabase: MovieAppDatabase): MovieDetailsDao {
        return movieAppDatabase.getMovieDetailsDao()
    }

}