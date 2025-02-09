package banquemisr.challenge05.data.remote

import banquemisr.challenge05.data.model.MovieDetailsResponse
import banquemisr.challenge05.data.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>


    @GET("popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>

    @GET("upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>

    @GET("{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: Long =0,
        @Query("language") language: String = "en-US"
    ): Response<MovieDetailsResponse>
}