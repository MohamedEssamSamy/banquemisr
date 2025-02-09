package banquemisr.challenge05.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("popular_movies_table")
data class PopularMovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val releaseDate: String,
    val posterImg: String
)
