package timisongdev.emproject.presentation

import android.annotation.SuppressLint
import timisongdev.emproject.R
import timisongdev.emproject.domain.model.Course
import timisongdev.emproject.databinding.ItemCourseBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

@SuppressLint("SetTextI18n")
fun courseAdapterDelegate(onLikeClick: (Int) -> Unit) =
    adapterDelegateViewBinding<Course, Course, ItemCourseBinding>(
        { layoutInflater, parent ->
            ItemCourseBinding.inflate(layoutInflater, parent, false)
        }
    ) {

        // Обработка нажатия на иконку сердцв
        binding.like.setOnClickListener {
            onLikeClick(item.id)
        }

        // Текст на карточке курса
        bind {
            binding.title.text = item.title
            binding.description.text = item.text
            binding.price.text = "${item.price} руб."
            binding.rate.text = "Оценка: ${item.rate}"
            binding.startDate.text = "Дата: ${item.startDate}"

            // Если сердце нажато
            val likeDrawable = if (item.hasLike) {
                R.drawable.ic_add_favorite
            } else {
                R.drawable.ic_favorite
            }
            binding.like.setImageResource(likeDrawable)
        }
    }