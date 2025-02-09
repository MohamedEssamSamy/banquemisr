package com.example.yourapp

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import banquemisr.challenge05.core.model.Genre
import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.movie.screens.MovieDetailScreen
import banquemisr.challenge05.movie.viewmodel.MovieDetailsViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.mockito.junit.MockitoJUnit

@RunWith(AndroidJUnit4::class)
class MovieDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Mock
    lateinit var movieDetailsViewModel: MovieDetailsViewModel

    private val movieDetailsStateFlow = MutableStateFlow<MovieDetails>(MovieDetails(
        0, "", "", "",
        emptyList(), "", 0
    ))


    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        whenever(movieDetailsViewModel.movieDetailsStateFlow).thenReturn(movieDetailsStateFlow)

        composeTestRule.setContent {
            MovieDetailScreen(
                context = context,
                navController = NavController(context),
                movieId = 1L,
                movieDetailsViewModel = movieDetailsViewModel
            )
        }
    }

    @Test
    fun testMovieDetailsScreenUI() = runBlocking {
         val genres: List<Genre> = listOf(Genre(id=28,name="Action"),Genre(id=27, name="Horror"),Genre(id=53, name="Thriller"))

        val mockMovieDetails =MovieDetails(
            970450,
            "Werewolves",
            "2024-12-04",
            "/cRTctVlwvMdXVsaYbX5qfkittDP.jpg",
            genres,
            "A year after a supermoon’s light activated a dormant gene, transforming humans into bloodthirsty werewolves and causing nearly a billion deaths, the nightmare resurfaces as the supermoon rises again.Two scientists attempt to stop the mutation but fail and must now struggle to reach one of their family homes.",
            94
        )
        movieDetailsStateFlow.emit(mockMovieDetails)

        composeTestRule.onNodeWithText("Movie Details").assertIsDisplayed()

        composeTestRule.onNodeWithText("Movie Title").assertIsDisplayed()

        composeTestRule.onNodeWithText("2025-02-01").assertIsDisplayed()

        composeTestRule.onNodeWithText("120 mins").assertIsDisplayed()

        composeTestRule.onNodeWithText("Action, Adventure").assertIsDisplayed()

        composeTestRule.onNodeWithText("This is a sample movie overview.").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed().performClick()
    }
}
