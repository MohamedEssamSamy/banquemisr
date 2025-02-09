package banquemisr.challenge05.data.model

data class MoviesResponse(
    val dates: Dates,
    val page: Long,
    val results: List<MovieModel>,
    val totalPages: Long,
    val totalResults: Long,
)