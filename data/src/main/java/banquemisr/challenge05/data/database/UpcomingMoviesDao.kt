package banquemisr.challenge05.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import banquemisr.challenge05.data.entity.UpcomingMovieEntity

@Dao
interface UpcomingMoviesDao {
    @Insert(entity = UpcomingMovieEntity::class, onConflict = OnConflictStrategy.REPLACE)
     fun insertNewMovie(upcomingMovieEntity: UpcomingMovieEntity)

    @Query("SELECT * FROM upcoming_movies_table")
     fun getAllMovies(): List<UpcomingMovieEntity>
}