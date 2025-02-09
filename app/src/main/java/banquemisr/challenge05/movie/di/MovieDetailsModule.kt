package banquemisr.challenge05.movie.di

import banquemisr.challenge05.core.repository.MovieDetailsRepository
import banquemisr.challenge05.core.usecase.GetMovieDetailsUseCase
import banquemisr.challenge05.data.database.MovieDetailsDao
import banquemisr.challenge05.data.remote.MovieDetailsRemoteDataSource
import banquemisr.challenge05.data.remote.TmdbApiService
import banquemisr.challenge05.data.repository.MovieDetailsRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MovieDetailsModule {

    @Provides
    fun provideMovieDetailsRemoteDataStore(tmdbApiService: TmdbApiService): MovieDetailsRemoteDataSource {
        return MovieDetailsRemoteDataSource(tmdbApiService)
    }

    @Provides
    fun provideMovieDetailsRepository(movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,movieDetailsDao: MovieDetailsDao
    ): MovieDetailsRepository {
        return MovieDetailsRepositoryImp(movieDetailsRemoteDataSource,movieDetailsDao)
    }

    @Provides
    fun provideGetMovieDetailsUseCase(movieDetailsRepository: MovieDetailsRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(movieDetailsRepository)
    }
}