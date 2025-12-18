package timisongdev.emproject.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timisongdev.emproject.data.repo.CourseRepositoryImpl
import timisongdev.emproject.domain.model.Course

class MenuViewModel(
    private val repository: CourseRepositoryImpl
) : ViewModel() {

    private val _originalCourses = MutableLiveData<List<Course>>(emptyList())
    private val _displayedCourses = MutableLiveData<List<Course>>(emptyList())
    val courses: LiveData<List<Course>> = _displayedCourses

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    var isSortedByDateDescending: Boolean = false

    private var currentSearchQuery: String = ""
    private var sortMode: SortMode = SortMode.PUBLISH_DATE_DESC

    init {
        loadCourses()
    }

    // Загружаем курсы
    fun loadCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getCourses().collect { fetchedCourses ->
                _originalCourses.value = fetchedCourses
                applyFiltersAndSort()
            }
            _isLoading.value = false
        }
    }

    // Добавление favorite
    fun toggleLike(courseId: Int) {
        viewModelScope.launch {
            val currentList = _originalCourses.value.orEmpty()
            val course = currentList.find { it.id == courseId } ?: return@launch
            val newHasLike = !course.hasLike

            repository.toggleFavorite(courseId, newHasLike)

            _originalCourses.value = currentList.map {
                if (it.id == courseId) it.copy(hasLike = newHasLike) else it
            }
            applyFiltersAndSort()
        }
    }

    // Сортировка по датам
    fun sortByDate() {
        isSortedByDateDescending = !isSortedByDateDescending
        sortMode = when (sortMode) {
            SortMode.PUBLISH_DATE_DESC -> SortMode.PUBLISH_DATE_ASC
            SortMode.PUBLISH_DATE_ASC -> SortMode.PUBLISH_DATE_DESC
        }
        applyFiltersAndSort()
    }

    // поиск по титлку
    fun searchByTitle(query: String) {
        currentSearchQuery = query.trim()
        applyFiltersAndSort()
    }

    // тут все жоска применяется
    private fun applyFiltersAndSort() {
        var list = _originalCourses.value.orEmpty()

        if (currentSearchQuery.isNotBlank()) {
            list = list.filter { it.title.contains(currentSearchQuery, ignoreCase = true) }
        }

        list = when (sortMode) {
            SortMode.PUBLISH_DATE_DESC -> list.sortedByDescending { it.publishDate }
            SortMode.PUBLISH_DATE_ASC -> list.sortedBy { it.publishDate }
        }

        _displayedCourses.value = list
    }
}

// Мод сортировки
enum class SortMode {
    PUBLISH_DATE_DESC,
    PUBLISH_DATE_ASC,
}