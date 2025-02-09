package banquemisr.challenge05.movie.di

import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.core.usecase.GetPopularMoviesUseCase
import banquemisr.challenge05.data.database.PopularMoviesDao
import banquemisr.challenge05.data.remote.PopularMoviesRemoteDataSource
import banquemisr.challenge05.data.remote.TmdbApiService
import banquemisr.challenge05.data.repository.PopularMoviesRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object PopularMoviesModule {

    @Provides
    fun providePopularMoviesRemoteDataSource(tmdbApiService: TmdbApiService):PopularMoviesRemoteDataSource{
        return PopularMoviesRemoteDataSource(tmdbApiService)
    }

    @Provides
    @Named("PopularMoviesRepositoryImp")
    fun providePopularMoviesRepository(popularMoviesRemoteDataSource: PopularMoviesRemoteDataSource, popularMoviesDao: PopularMoviesDao):MoviesRepository{
        return PopularMoviesRepositoryImp(popularMoviesRemoteDataSource,popularMoviesDao)
    }

    @Provides
    fun provideGetPopularMoviesUseCase(@Named("PopularMoviesRepositoryImp") popularMoviesRepository: MoviesRepository):GetPopularMoviesUseCase{
        return GetPopularMoviesUseCase(popularMoviesRepository)
    }
}