package timisongdev.emproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import timisongdev.emproject.domain.model.CourseEntity

@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}