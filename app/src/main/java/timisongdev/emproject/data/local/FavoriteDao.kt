package timisongdev.emproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import timisongdev.emproject.domain.model.CourseEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(course: CourseEntity)

    @Query("DELETE FROM favorite_courses WHERE id = :courseId")
    suspend fun removeFromFavorite(courseId: Int)

    @Query("SELECT * FROM favorite_courses")
    fun getAllFavorites(): Flow<List<CourseEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_courses WHERE id = :courseId)")
    suspend fun isFavorite(courseId: Int): Boolean
}