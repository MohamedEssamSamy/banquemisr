package banquemisr.challenge05.movie

import banquemisr.challenge05.core.exception.DomainError
import banquemisr.challenge05.core.exception.ErrorType
import banquemisr.challenge05.core.model.Genre
import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.repository.MovieDetailsRepository
import banquemisr.challenge05.core.usecase.GetMovieDetailsUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetMovieDetailsUseCaseTest {
    @Mock
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    @InjectMocks
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    private val movieId = 970450L
    private val language = "en"

    /*
    * Data of MovieDetails :-
    *   id=970450,
        title=Werewolves,
        releaseDate=2024-12-04,
        posterImg=/cRTctVlwvMdXVsaYbX5qfkittDP.jpg,
        genres=[Genre(id=28, name=Action), Genre(id=27, name=Horror), Genre(id=53, name=Thriller)],
        overview=A year after a supermoon’s light activated a dormant gene, transforming humans into bloodthirsty werewolves and causing nearly a billion deaths, the nightmare resurfaces as the supermoon rises again.Two scientists attempt to stop the mutation but fail and must now struggle to reach one of their family homes.,
        runtime=94
    */

    private val genres: List<Genre> = listOf(Genre(id=28,name="Action"),Genre(id=27, name="Horror"),Genre(id=53, name="Thriller"))

    private val movieDetails = MovieDetails(
        970450,
        "Werewolves",
        "2024-12-04",
        "/cRTctVlwvMdXVsaYbX5qfkittDP.jpg",
        genres,
        "A year after a supermoon’s light activated a dormant gene, transforming humans into bloodthirsty werewolves and causing nearly a billion deaths, the nightmare resurfaces as the supermoon rises again.Two scientists attempt to stop the mutation but fail and must now struggle to reach one of their family homes.",
        94
        )

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    class MainCoroutineRule : TestWatcher() {
        val testDispatcher = TestCoroutineDispatcher()
        val testScope = TestCoroutineScope(testDispatcher)

        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
            testScope.cleanupTestCoroutines()
        }
    }

    @Test
    fun `invoke should emit success and insert data when repository call succeeds`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        whenever(movieDetailsRepository.getMovieDetails(movieId, language))
            .thenReturn(flow { emit(Result.Success(movieDetails)) })

        whenever(movieDetailsRepository.insertNewMovieDetailsInsideDatabase(movieDetails))
            .thenReturn(flow {})

        val results = mutableListOf<Result<MovieDetails>>()

        // Act
        getMovieDetailsUseCase(movieId, language).collect {
            results.add(it)
        }

        // Assert
        assertEquals(1, results.size)
        assertTrue(results[0] is Result.Success)
        verify(movieDetailsRepository).insertNewMovieDetailsInsideDatabase(movieDetails)
    }

    @Test
    fun `invoke should emit success then database error when insertion fails`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val exception = Exception("Insert failed")
        whenever(movieDetailsRepository.getMovieDetails(movieId, language))
            .thenReturn(flow { emit(Result.Success(movieDetails)) })

        whenever(movieDetailsRepository.insertNewMovieDetailsInsideDatabase(movieDetails))
            .thenReturn(flow { throw exception })

        val results = mutableListOf<Result<MovieDetails>>()

        // Act
        getMovieDetailsUseCase(movieId, language).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Success)
        assertTrue(results[1] is Result.Error)

//        val error = (results[1] as Result.Error).error
//        assertTrue(error is ErrorType.LocalDatabaseError)
//        assertTrue((error as ErrorType.LocalDatabaseError).error is DatabaseError.IOException)
    }

    @Test
    fun `invoke should emit database success then original error when repository fails`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val remoteError = DomainError.Server(500, "Server Error")
        whenever(movieDetailsRepository.getMovieDetails(movieId, language))
            .thenReturn(flow { emit(Result.Error(ErrorType.RemoteError(remoteError))) })
        whenever(movieDetailsRepository.getMovieDetailsFromDatabase(movieId))
            .thenReturn(flow { emit(Result.Success(movieDetails)) })

        val results = mutableListOf<Result<MovieDetails>>()

        // Act
        getMovieDetailsUseCase(movieId, language).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Success)
        assertTrue(results[1] is Result.Error)

        val error = (results[1] as Result.Error).error
        assertTrue(error is ErrorType.RemoteError)
        assertEquals(remoteError, (error as ErrorType.RemoteError).error)
    }

    @Test
    fun `invoke should emit original error then database error when fallback fails`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val remoteError = DomainError.Server(500, "Server Error")
        val dbException = Exception("DB fetch failed")
        whenever(movieDetailsRepository.getMovieDetails(movieId, language))
            .thenReturn(flow { emit(Result.Error(ErrorType.RemoteError(remoteError))) })
        whenever(movieDetailsRepository.getMovieDetailsFromDatabase(movieId))
            .thenReturn(flow { throw dbException })

        val results = mutableListOf<Result<MovieDetails>>()

        // Act
        getMovieDetailsUseCase(movieId, language).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Error)
        assertTrue(results[1] is Result.Error)

        val firstError = (results[0] as Result.Error).error
        assertTrue(firstError is ErrorType.RemoteError)

        val secondError = (results[1] as Result.Error).error
        assertTrue(secondError is ErrorType.LocalDatabaseError)
    }

    @Test
    fun `invoke should handle multiple emissions from repository`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val modifiedDetails = movieDetails.copy(title = "Modified Title")
        whenever(movieDetailsRepository.getMovieDetails(movieId, language))
            .thenReturn(flow {
                emit(Result.Success(movieDetails))
                emit(Result.Success(modifiedDetails))
            })
        whenever(movieDetailsRepository.insertNewMovieDetailsInsideDatabase(any()))
            .thenReturn(flow {
//                emit(Unit)
            })

        val results = mutableListOf<Result<MovieDetails>>()

        // Act
        getMovieDetailsUseCase(movieId, language).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        verify(movieDetailsRepository, times(2)).insertNewMovieDetailsInsideDatabase(any())
        assertTrue(results.all { it is Result.Success })
        assertEquals(movieDetails, (results[0] as Result.Success).data)
        assertEquals(modifiedDetails, (results[1] as Result.Success).data)
    }

}