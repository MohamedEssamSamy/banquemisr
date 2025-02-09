package banquemisr.challenge05.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import banquemisr.challenge05.core.model.Genre

@Entity("movie_details_table")
data class MovieDetailsEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val releaseDate: String,
    val posterImg: String,
    val genres: List<Genre>,
    val overview: String,
    val runtime: Long,
)