package banquemisr.challenge05.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("now_playing_movies_table")
data class NowPlayingMovieEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val releaseDate: String,
    val posterImg: String
)