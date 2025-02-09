package banquemisr.challenge05.movie

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import banquemisr.challenge05.core.model.Movie
import banquemisr.challenge05.movie.screens.DisplayErrorMessageAsToast
import banquemisr.challenge05.movie.screens.LoadingIndicator
import banquemisr.challenge05.movie.screens.MovieDetailScreen
import banquemisr.challenge05.movie.ui.theme.BackgroundColor
import banquemisr.challenge05.movie.viewmodel.MovieDetailsViewModel
import banquemisr.challenge05.movie.viewmodel.NowPlayingMoviesViewModel
import banquemisr.challenge05.movie.viewmodel.PopularMoviesViewModel
import banquemisr.challenge05.movie.viewmodel.UpcomingMoviesViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "MainActivityTAG"

    private val nowPlayingMoviesViewModel: NowPlayingMoviesViewModel by viewModels()
    private val popularMoviesViewModel: PopularMoviesViewModel by viewModels()
    private val upcomingMoviesViewModel: UpcomingMoviesViewModel by viewModels()
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = BackgroundColor) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "movie_list_screen") {
                        composable("movie_list_screen") {
                            MovieListScreen(navController)
                        }
                        composable("movie_details_screen/{movieId}") { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getString("movieId")?.toLong() ?: 0L
                            MovieDetailScreen(baseContext,navController, movieId,movieDetailsViewModel)
                        }
                    }
                }
            }


        }
    }

    // MovieListScreen.kt
    @Composable
    fun MovieListScreen(navController: NavController) {
        var selectedTab by remember { mutableIntStateOf(0) }
        val tabs = listOf("Now Playing", "Popular", "Upcoming")

        Scaffold(
            topBar = {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    tabs.forEachIndexed { index, title ->

                        // fetch data
                        when(index){
                            0->nowPlayingMoviesViewModel.fetchNowPlayingMovies()
                            1->popularMoviesViewModel.fetchPopularMovies()
                            else->upcomingMoviesViewModel.fetchUpcomingMovies()
                        }

                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title.uppercase()) },
                            icon = {
                                Icon(
                                    imageVector = when (index) {
                                        0 -> Icons.Default.PlayArrow
                                        1 -> Icons.Default.Star
                                        else -> Icons.Default.DateRange
                                    },
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        ) { padding ->
            val moviesState = when (selectedTab) {
                0 -> {
                    DisplayErrorMessageAsToast(baseContext,nowPlayingMoviesViewModel.errorMessage)
                    nowPlayingMoviesViewModel.nowPlayingMoviesStateFlow.collectAsState()
                }
                1 ->
                {
                    DisplayErrorMessageAsToast(baseContext,popularMoviesViewModel.errorMessage)
                    popularMoviesViewModel.popularMoviesStateFlow.collectAsState()
                }
                else ->
                {
                    DisplayErrorMessageAsToast(baseContext,upcomingMoviesViewModel.errorMessage)
                    upcomingMoviesViewModel.upcomingMoviesStateFlow.collectAsState()
                }
            }
            MovieHorizontalList(movies = moviesState.value, navController, padding)
        }
    }

    @Composable
    private fun MovieHorizontalList(
        movies: List<Movie>,
        navController: NavController,
        padding: PaddingValues
    ) {
        LazyRow(
            contentPadding = padding,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            items(movies.size) { index ->
                MovieCard(movie = movies[index], navController)
            }
        }
    }

    @Composable
    fun MovieCard(movie: Movie, navController: NavController) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .width(300.dp)
                .clickable {
                    navController.navigate("movie_details_screen/${movie.id}")
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterImg)
                        .crossfade(true)
                        .build(),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = movie.releaseDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
//
//    @Composable
//    private fun MovieGrid(
//        movies: List<Movie>,
//        navController: NavController,
//        padding: PaddingValues
//    ) {
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(minSize = 180.dp),
//            contentPadding = padding,
//            modifier = Modifier.background(MaterialTheme.colorScheme.background)
//        ) {
//            items(movies.size) { index ->
//                MovieCard(movie = movies.get(index),navController)
//            }
//        }
//    }
//
//
//    @Composable
//     fun MovieCard(movie: Movie,navController: NavController) {
//        Card(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth()
//                .clickable {
//                    navController.navigate("movie_details_screen/${movie.id}")
//                },
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//            shape = MaterialTheme.shapes.medium
//        ) {
//            Column {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(movie.posterImg)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = movie.title,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(2f / 3f),
//                    contentScale = ContentScale.Crop
//                )
//
//                Column(modifier = Modifier.padding(12.dp)) {
//                    Text(
//                        text = movie.title,
//                        style = MaterialTheme.typography.titleMedium,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = movie.releaseDate,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                    )
//                }
//            }
//        }
//    }
}



