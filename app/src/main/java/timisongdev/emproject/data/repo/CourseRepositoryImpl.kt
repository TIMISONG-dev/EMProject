package timisongdev.emproject.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timisongdev.emproject.data.local.FavoriteDao
import timisongdev.emproject.data.remote.CoursesApi
import timisongdev.emproject.data.remote.toDomain
import timisongdev.emproject.domain.repo.CourseRepository
import timisongdev.emproject.domain.model.Course
import timisongdev.emproject.domain.model.CourseEntity

class CourseRepositoryImpl(
    private val api: CoursesApi,
    private val favoriteDao: FavoriteDao
) : CourseRepository {

    override suspend fun getCourses(): Flow<List<Course>> = flow {
        try {
            val response = api.getCourses()
            val networkCourses = response.courses.toDomain()

            val favoriteIds = favoriteDao.getAllFavorites()
                .first()
                .map { it.id }
                .toSet()

            val mergedCourses = networkCourses.map { course ->
                course.copy(hasLike = favoriteIds.contains(course.id))
            }

            emit(mergedCourses)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun toggleFavorite(courseId: Int, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.addToFavorite(CourseEntity(id = courseId))
        } else {
            favoriteDao.removeFromFavorite(courseId)
        }
    }
}