package banquemisr.challenge05.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("upcoming_movies_table")
data class UpcomingMovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val releaseDate: String,
    val posterImg: String
)
