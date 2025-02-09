package banquemisr.challenge05.movie.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import banquemisr.challenge05.core.model.MovieDetails
import banquemisr.challenge05.movie.R
import banquemisr.challenge05.movie.viewmodel.MovieDetailsViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.MutableSharedFlow

// MovieDetailScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(context:Context , navController: NavController, movieId: Long,movieDetailsViewModel:MovieDetailsViewModel) {
    DisplayErrorMessageAsToast(context,movieDetailsViewModel.errorMessage)
    val movieDetailsState by movieDetailsViewModel.movieDetailsStateFlow.collectAsState()
    LaunchedEffect(movieId) {
        movieDetailsViewModel.fetchMovieDetails(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        MovieDetailsContent(movieDetailsState, padding)
    }
}

@Composable
private fun MovieDetailsContent(details: MovieDetails, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(details.posterImg)
                .crossfade(true)
                .build(),
            contentDescription = details.title,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Fit
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = details.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Default.DateRange, text = details.releaseDate)
            InfoRow(icon = ImageVector.vectorResource(R.drawable.baseline_access_time_24), text = "${details.runtime} mins")
            InfoRow(
                icon = Icons.Default.Info,
                text = details.genres.joinToString { it.name }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = details.overview,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}


@Composable
fun DisplayErrorMessageAsToast( context:Context ,dataState: MutableSharedFlow<String>) {
    LaunchedEffect(Unit) {
        dataState.collect{
            showToast(context, it)
        }
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
