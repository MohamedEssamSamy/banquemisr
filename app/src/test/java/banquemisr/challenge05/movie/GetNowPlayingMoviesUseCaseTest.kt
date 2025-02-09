package banquemisr.challenge05.movie

import banquemisr.challenge05.core.exception.DatabaseError
import banquemisr.challenge05.core.exception.DomainError
import banquemisr.challenge05.core.exception.ErrorType
import banquemisr.challenge05.core.model.Movie
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
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import banquemisr.challenge05.core.model.Result
import banquemisr.challenge05.core.repository.MoviesRepository
import banquemisr.challenge05.core.usecase.GetNowPlayingMoviesUseCase
import javax.inject.Named

@RunWith(MockitoJUnitRunner::class)
class GetNowPlayingMoviesUseCaseTest {

    @Mock
    @Named("NowPlayingMoviesRepositoryImp")
    private lateinit var moviesRepository: MoviesRepository

    @InjectMocks
    private lateinit var getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val language = "en"
    private val page = 1

    /*
             Movie(
                    id=970450,
                    title=Werewolves,
                    releaseDate=2024-12-04,
                    posterImg=/cRTctVlwvMdXVsaYbX5qfkittDP.jpg
                )
    */
    private val mockMovies = listOf(
        Movie(
            id = 970450,
            title = "Werewolves",
            releaseDate = "2024-12-04",
            posterImg = "/cRTctVlwvMdXVsaYbX5qfkittDP.jpg"
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
    fun `invoke should emit remote success and insert data`() =
        coroutineRule.testScope.runBlockingTest {
            // Arrange
            whenever(
                moviesRepository.getMoviesFromRemote(
                    language, page
                )
            ).thenReturn(flow { emit(Result.Success(mockMovies)) })
            whenever(moviesRepository.insertNewMovieInsideDatabase(mockMovies)).thenReturn(flow {})

            val results = mutableListOf<Result<List<Movie>>>()

            // Act
            getNowPlayingMoviesUseCase(language, page).collect { results.add(it) }

            // Assert
            assertEquals(1, results.size)
            assertTrue(results[0] is Result.Success)
            verify(moviesRepository).insertNewMovieInsideDatabase(mockMovies)
        }

    @Test
    fun `invoke should emit success then database error on insert failure`() =
        coroutineRule.testScope.runBlockingTest {
            // Arrange
            val exception = Exception("Insert failed")
            whenever(moviesRepository.getMoviesFromRemote(language, page)).thenReturn(flow {
                emit(
                    Result.Success(mockMovies)
                )
            })
            whenever(moviesRepository.insertNewMovieInsideDatabase(mockMovies)).thenReturn(flow { throw exception })

            val results = mutableListOf<Result<List<Movie>>>()

            // Act
            getNowPlayingMoviesUseCase(language, page).collect { results.add(it) }

            // Assert
            assertEquals(2, results.size)
            assertTrue(results[0] is Result.Success)
            assertTrue(results[1] is Result.Error)

//            val error = (results[1] as Result.Error).error
//            assertTrue(error is ErrorType.LocalDatabaseError)
//            assertTrue((error as ErrorType.LocalDatabaseError).error is DatabaseError.IOException)
        }

    @Test
    fun `invoke should emit database results then remote error on remote failure`() =
        coroutineRule.testScope.runBlockingTest {
            // Arrange
            val remoteError = DomainError.Server(500, "Server Error")
            whenever(moviesRepository.getMoviesFromRemote(language, page)).thenReturn(flow {
                emit(
                    Result.Error(ErrorType.RemoteError(remoteError))
                )
            })
            whenever(moviesRepository.getAllMoviesFromDatabase()).thenReturn(flow {
                emit(
                    Result.Success(
                        mockMovies
                    )
                )
            })

            val results = mutableListOf<Result<List<Movie>>>()

            // Act
            getNowPlayingMoviesUseCase(language, page).collect { results.add(it) }

            // Assert
            assertEquals(2, results.size)
            assertTrue(results[0] is Result.Success)
            assertTrue(results[1] is Result.Error)

            val error = (results[1] as Result.Error).error
            assertEquals(remoteError, (error as ErrorType.RemoteError).error)
        }

    @Test
    fun `invoke should emit remote error then database error on complete failure`() =
        coroutineRule.testScope.runBlockingTest {
            // Arrange
            val remoteError = DomainError.Server(500, "Server Error")
            val dbException = Exception("DB failure")
            whenever(moviesRepository.getMoviesFromRemote(language, page)).thenReturn(flow {
                emit(
                    Result.Error(ErrorType.RemoteError(remoteError))
                )
            })
            whenever(moviesRepository.getAllMoviesFromDatabase()).thenReturn(flow { throw dbException })

            val results = mutableListOf<Result<List<Movie>>>()

            // Act
            getNowPlayingMoviesUseCase(language, page).collect { results.add(it) }

            // Assert
            assertEquals(2, results.size)
            assertTrue(results[0] is Result.Error)
            assertTrue(results[1] is Result.Error)

            val firstError = (results[0] as Result.Error).error
            assertTrue(firstError is ErrorType.RemoteError)

            val secondError = (results[1] as Result.Error).error
            assertTrue(secondError is ErrorType.LocalDatabaseError)
        }
}