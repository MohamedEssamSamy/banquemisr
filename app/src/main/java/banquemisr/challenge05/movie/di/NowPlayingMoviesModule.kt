package banquemisr.challenge05.movie.di

import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.core.usecase.GetNowPlayingMoviesUseCase
import banquemisr.challenge05.data.database.NowPlayingMoviesDao
import banquemisr.challenge05.data.remote.NowPlayingMoviesRemoteDataSource
import banquemisr.challenge05.data.remote.TmdbApiService
import banquemisr.challenge05.data.repository.NowPlayingMoviesRepositoryImp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named


@Module
@InstallIn(ViewModelComponent::class)
object NowPlayingMoviesModule {

    @Provides
    fun provideNowPlayingMoviesRemoteDataSource(tmdbApiService: TmdbApiService): NowPlayingMoviesRemoteDataSource {
        return NowPlayingMoviesRemoteDataSource(tmdbApiService)
    }

    @Provides
    @Named("NowPlayingMoviesRepositoryImp")
    fun provideNowPlayingMoviesRepository(nowPlayingMoviesRemoteDataSource: NowPlayingMoviesRemoteDataSource,nowPlayingMoviesDao: NowPlayingMoviesDao): MoviesRepository {
        return NowPlayingMoviesRepositoryImp(nowPlayingMoviesRemoteDataSource,nowPlayingMoviesDao)
    }

    @Provides
    fun provideGetNowPlayingMoviesUseCase(@Named("NowPlayingMoviesRepositoryImp") nowPlayingMoviesRepository: MoviesRepository): GetNowPlayingMoviesUseCase {
        return GetNowPlayingMoviesUseCase(nowPlayingMoviesRepository)
    }

}