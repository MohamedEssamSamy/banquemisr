package banquemisr.challenge05.movie
import banquemisr.challenge05.core.exception.DatabaseError
import banquemisr.challenge05.core.exception.DomainError
import banquemisr.challenge05.core.exception.ErrorType
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.core.usecase.GetUpcomingMoviesUseCase
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
import javax.inject.Named

@RunWith(MockitoJUnitRunner::class)
class GetUpcomingMoviesUseCaseTest {

    @Mock
    @Named("UpcomingMoviesRepositoryImp")
    private lateinit var moviesRepository: MoviesRepository

    @InjectMocks
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val language = "en"
    private val page = 1
    /*
    * Movie(
            id=426063,
            title=Nosferatu,
            releaseDate=2024-12-25,
            posterImg=/5qGIxdEO841C0tdY8vOdLoRVrr0.jpg
        )
    * */
    private val mockMovies = listOf(
        Movie(
            id=426063,
            title="Nosferatu",
            releaseDate="2024-12-25",
            posterImg="/5qGIxdEO841C0tdY8vOdLoRVrr0.jpg"
    )
    )

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
    fun `invoke should emit remote success and insert data`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        whenever(moviesRepository.getMoviesFromRemote(language, page))
            .thenReturn(flow { emit(Result.Success(mockMovies)) })
        whenever(moviesRepository.insertNewMovieInsideDatabase(mockMovies))
            .thenReturn(flow { })

        val results = mutableListOf<Result<List<Movie>>>()

        // Act
        getUpcomingMoviesUseCase(language, page).collect { results.add(it) }

        // Assert
        assertEquals(1, results.size)
        assertTrue(results[0] is Result.Success)
        verify(moviesRepository).insertNewMovieInsideDatabase(mockMovies)
    }

    @Test
    fun `invoke should emit success then database error on insert failure`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val exception = Exception("Database insertion failed")
        whenever(moviesRepository.getMoviesFromRemote(language, page))
            .thenReturn(flow { emit(Result.Success(mockMovies)) })
        whenever(moviesRepository.insertNewMovieInsideDatabase(mockMovies))
            .thenReturn(flow { throw exception })

        val results = mutableListOf<Result<List<Movie>>>()

        // Act
        getUpcomingMoviesUseCase(language, page).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Success)
        assertTrue(results[1] is Result.Error)

//        val error = (results[1] as Result.Error).error
//        assertTrue(error is ErrorType.LocalDatabaseError)
//        assertTrue((error as ErrorType.LocalDatabaseError).error is DatabaseError.IOException)
    }

    @Test
    fun `invoke should emit database results then remote error on remote failure`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val remoteError = DomainError.Network("Network unavailable")
        whenever(moviesRepository.getMoviesFromRemote(language, page))
            .thenReturn(flow { emit(Result.Error(ErrorType.RemoteError(remoteError))) })
        whenever(moviesRepository.getAllMoviesFromDatabase())
            .thenReturn(flow { emit(Result.Success(mockMovies)) })

        val results = mutableListOf<Result<List<Movie>>>()

        // Act
        getUpcomingMoviesUseCase(language, page).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Success)
        assertTrue(results[1] is Result.Error)
        assertEquals(mockMovies, (results[0] as Result.Success).data)
        assertEquals(remoteError, (results[1] as Result.Error).error.let { it as ErrorType.RemoteError }.error)
    }

    @Test
    fun `invoke should emit remote error then database error on complete failure`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val remoteError = DomainError.Unknown("Unknown error")
        val dbException = Exception("Database corruption")
        whenever(moviesRepository.getMoviesFromRemote(language, page))
            .thenReturn(flow { emit(Result.Error(ErrorType.RemoteError(remoteError))) })
        whenever(moviesRepository.getAllMoviesFromDatabase())
            .thenReturn(flow { throw dbException })

        val results = mutableListOf<Result<List<Movie>>>()

        // Act
        getUpcomingMoviesUseCase(language, page).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Error)
        assertTrue(results[1] is Result.Error)
        assertTrue((results[0] as Result.Error).error is ErrorType.RemoteError)
        assertTrue((results[1] as Result.Error).error is ErrorType.LocalDatabaseError)
    }

    @Test
    fun `invoke should handle empty database on fallback`() = coroutineRule.testScope.runBlockingTest {
        // Arrange
        val remoteError = DomainError.Server(404, "Not Found")
        whenever(moviesRepository.getMoviesFromRemote(language, page))
            .thenReturn(flow { emit(Result.Error(ErrorType.RemoteError(remoteError))) })
        whenever(moviesRepository.getAllMoviesFromDatabase())
            .thenReturn(flow { emit(Result.Success(emptyList())) })

        val results = mutableListOf<Result<List<Movie>>>()

        // Act
        getUpcomingMoviesUseCase(language, page).collect { results.add(it) }

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Success)
        assertTrue((results[0] as Result.Success).data.isEmpty())
        assertTrue(results[1] is Result.Error)
    }
}