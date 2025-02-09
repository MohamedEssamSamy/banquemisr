package banquemisr.challenge05.movie.di

import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.core.usecase.GetUpcomingMoviesUseCase
import banquemisr.challenge05.data.database.UpcomingMoviesDao
import banquemisr.challenge05.data.remote.TmdbApiService
import banquemisr.challenge05.data.remote.UpcomingMoviesRemoteDataSource
import banquemisr.challenge05.data.repository.UpcomingMoviesRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object UpcomingMoviesModule {

    @Provides
    fun provideUpcomingMoviesRemoteDataSource(tmdbApiService: TmdbApiService): UpcomingMoviesRemoteDataSource {
        return UpcomingMoviesRemoteDataSource(tmdbApiService)
    }

    @Provides
    @Named("UpcomingMoviesRepositoryImp")
    fun provideUpcomingMoviesRepository(upcomingMoviesRemoteDataSource: UpcomingMoviesRemoteDataSource, upcomingMoviesDao: UpcomingMoviesDao): MoviesRepository {
        return UpcomingMoviesRepositoryImp(upcomingMoviesRemoteDataSource,upcomingMoviesDao)
    }

    @Provides
    fun provideUp(@Named("UpcomingMoviesRepositoryImp") upcomingMoviesRepository: MoviesRepository): GetUpcomingMoviesUseCase {
        return GetUpcomingMoviesUseCase(upcomingMoviesRepository)
    }
}