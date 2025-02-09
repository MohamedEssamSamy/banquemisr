package banquemisr.challenge05.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import banquemisr.challenge05.data.entity.MovieDetailsEntity


@Dao
interface MovieDetailsDao {

    @Insert(entity = MovieDetailsEntity::class, onConflict = OnConflictStrategy.REPLACE)
     fun insertNewMovie(movieDetailsEntity: MovieDetailsEntity)

    @Query("SELECT * FROM movie_details_table WHERE id = :movieId LIMIT 1")
    fun getMovieDetails(movieId: Long):MovieDetailsEntity?
}