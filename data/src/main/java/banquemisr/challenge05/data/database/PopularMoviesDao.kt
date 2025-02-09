package banquemisr.challenge05.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import banquemisr.challenge05.data.entity.PopularMovieEntity

@Dao
interface PopularMoviesDao {

    @Insert(entity = PopularMovieEntity::class, onConflict = OnConflictStrategy.REPLACE)
     fun insertNewMovie(popularMovieEntity: PopularMovieEntity)

    @Query("SELECT * FROM popular_movies_table")
     fun getAllMovies():List<PopularMovieEntity>
}