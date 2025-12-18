package timisongdev.emproject.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    val hasLike: Boolean = true
)