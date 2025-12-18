package timisongdev.emproject.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timisongdev.emproject.data.local.AppDatabase
import timisongdev.emproject.data.remote.CoursesApi
import timisongdev.emproject.data.repo.CourseRepositoryImpl
import timisongdev.emproject.domain.repo.CourseRepository
import timisongdev.emproject.presentation.MenuViewModel

// Для получения инфы из gdrive
val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(CoursesApi::class.java) }
}

// Room
val repositoryModule = module {
    single { CourseRepositoryImpl(get(), get()) }

    single<CourseRepository> { get<CourseRepositoryImpl>() }
}

// Koin ViewModel
val viewModelModule = module {
    viewModel {
        MenuViewModel(
            get()
        )
    }
}

// Room
val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "favorite_db"
        ).build()
    }
    single { get<AppDatabase>().favoriteDao() }
}

val appModules = listOf(
    networkModule, databaseModule,
    repositoryModule, viewModelModule
)