package timisongdev.emproject.domain.repo

import kotlinx.coroutines.flow.Flow
import timisongdev.emproject.domain.model.Course

// Получаем курсы
interface CourseRepository {
    suspend fun getCourses(): Flow<List<Course>>
}