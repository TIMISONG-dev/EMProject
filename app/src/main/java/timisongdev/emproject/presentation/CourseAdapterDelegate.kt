package timisongdev.emproject.presentation

import timisongdev.emproject.R
import timisongdev.emproject.domain.model.Course
import timisongdev.emproject.databinding.ItemCourseBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun courseAdapterDelegate(onLikeClick: (Int) -> Unit) =
    adapterDelegateViewBinding<Course, Course, ItemCourseBinding>(
        { layoutInflater, parent ->
            ItemCourseBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.like.setOnClickListener {
            onLikeClick(item.id)
        }

        bind {
            binding.title.text = item.title
            binding.description.text = item.text
            binding.price.text = "${item.price} руб."
            binding.rate.text = "Оценка: ${item.rate}"
            binding.startDate.text = "Дата: ${item.startDate}"

            val likeDrawable = if (item.hasLike) {
                R.drawable.ic_add_favorite
            } else {
                R.drawable.ic_favorite
            }
            binding.like.setImageResource(likeDrawable)
        }
    }