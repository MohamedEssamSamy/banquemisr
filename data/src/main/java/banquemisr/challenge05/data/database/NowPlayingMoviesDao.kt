package banquemisr.challenge05.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import banquemisr.challenge05.data.entity.NowPlayingMovieEntity

@Dao
interface NowPlayingMoviesDao {

    @Insert(entity = NowPlayingMovieEntity::class, onConflict = OnConflictStrategy.REPLACE)
     fun insertNewMovie(nowPlayingMovieEntity: NowPlayingMovieEntity)

    @Query("SELECT * FROM now_playing_movies_table")
     fun getAllMovies():List<NowPlayingMovieEntity>

}