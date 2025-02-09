package banquemisr.challenge05.movie.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import banquemisr.challenge05.data.remote.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Named("token")
    fun provideToken(): String {
        return "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlYjliZGFhZDIzZjExOTFmMjg2NGFiNmFiZTNjZmZjMyIsIm5iZiI6MTczODU3MTkwNS4zNDQsInN1YiI6IjY3YTA4MDgxN2ViYjA2MTRmZjI2NmJmNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.c48U3ebPGdK63b6KBF5kw3TvHzcXEt3oBX77N-bvuDQ"
    }

    @Provides
    @Named("base_url")
    fun provideBaseURL(): String {
        return "https://api.themoviedb.org/3/movie/"
    }

    @Provides
    fun provideOkHttpClient(
        @Named("token") token: String, @ApplicationContext context: Context
    ): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024
        val cache = Cache(File(context.cacheDir, "http_cache"), cacheSize.toLong())
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
            var originalRequest = chain.request()
            if (!isNetworkAvailable(context)) {
                originalRequest = originalRequest.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=604800").build()
            }
            val newRequest =
                originalRequest.newBuilder().header("Authorization", "Bearer $token").build()
            chain.proceed(newRequest)
        }.build()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        )
    }

    @Provides
    fun provideTmdbApiService(
        @Named("base_url") baseURL: String, okHttpClient: OkHttpClient
    ): TmdbApiService {
        return Retrofit.Builder().baseUrl(baseURL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(TmdbApiService::class.java)
    }

}