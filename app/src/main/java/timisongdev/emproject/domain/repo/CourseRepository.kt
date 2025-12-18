package timisongdev.emproject.domain.repo

import kotlinx.coroutines.flow.Flow
import timisongdev.emproject.domain.model.Course

interface CourseRepository {
    suspend fun getCourses(): Flow<List<Course>>
}