package banquemisr.challenge05.core.model

data class MovieDetails(
    val id: Long,
    val title: String,
    val releaseDate: String,
    val posterImg: String,
    val genres: List<Genre>,
    val overview: String,
    val runtime: Long,
)
