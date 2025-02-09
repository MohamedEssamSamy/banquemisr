package com.example.yourapp

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.movie.screens.MovieDetailScreen
import banquemisr.challenge05.movie.ui.theme.BackgroundColor
import banquemisr.challenge05.movie.viewmodel.NowPlayingMoviesViewModel
import banquemisr.challenge05.movie.viewmodel.PopularMoviesViewModel
import banquemisr.challenge05.movie.viewmodel.UpcomingMoviesViewModel
import banquemisr.challenge05.movie.MainActivity

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class MovieListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Mock
    lateinit var nowPlayingMoviesViewModel: NowPlayingMoviesViewModel

    @Mock
    lateinit var popularMoviesViewModel: PopularMoviesViewModel

    @Mock
    lateinit var upcomingMoviesViewModel: UpcomingMoviesViewModel

    private val nowPlayingMoviesStateFlow = MutableStateFlow(listOf<Movie>())
    private val popularMoviesStateFlow = MutableStateFlow(listOf<Movie>())
    private val upcomingMoviesStateFlow = MutableStateFlow(listOf<Movie>())

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        whenever(nowPlayingMoviesViewModel.nowPlayingMoviesStateFlow).thenReturn(nowPlayingMoviesStateFlow)
        whenever(popularMoviesViewModel.popularMoviesStateFlow).thenReturn(popularMoviesStateFlow)
        whenever(upcomingMoviesViewModel.upcomingMoviesStateFlow).thenReturn(upcomingMoviesStateFlow)

        composeTestRule.setContent {
            MainActivity().MovieListScreen(
                navController = NavController(context)
            )
        }
    }

    @Test
    fun testMovieListScreenUI() {
        val mockMovies = listOf(
            Movie(
                id = 970450,
                title = "Werewolves",
                releaseDate = "2024-12-04",
                posterImg = "/cRTctVlwvMdXVsaYbX5qfkittDP.jpg"
            )
        )

        nowPlayingMoviesStateFlow.value = mockMovies
        popularMoviesStateFlow.value = mockMovies
        upcomingMoviesStateFlow.value = mockMovies

        composeTestRule.onNodeWithText("Now Playing").assertIsDisplayed()
        composeTestRule.onNodeWithText("Popular").assertIsDisplayed()
        composeTestRule.onNodeWithText("Upcoming").assertIsDisplayed()

        composeTestRule.onNodeWithText("Movie 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Popular").performClick()
        composeTestRule.onNodeWithText("Movie 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Upcoming").performClick()
        composeTestRule.onNodeWithText("Movie 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Movie 1").performClick()
    }
}
