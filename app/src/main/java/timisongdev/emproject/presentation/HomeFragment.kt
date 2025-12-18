package timisongdev.emproject.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timisongdev.emproject.R
import timisongdev.emproject.databinding.FragmentHomeBinding
import timisongdev.emproject.domain.model.Course

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModel()

    // Адаптер для RecyclerView
    private val adapter = ListDelegationAdapter(
        AdapterDelegatesManager<List<Course>>()
            .addDelegate(courseAdapterDelegate { courseId ->
                viewModel.toggleLike(courseId)
            })
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        // SwipeRefresh обновляет список
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadCourses()
        }

        binding.sort.setOnClickListener {
            viewModel.sortByDate()
            updateSortIcon()
        }

        // Поиск по курсам
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchByTitle(s.toString().trim())
            }
        })

        // Чекаем список курсов из viewmodel
        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            adapter.items = courses
            adapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.swipeRefresh.isRefreshing = loading
        }

        updateSortIcon()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCourses()
    }

    // Если сортировка включена
    private fun updateSortIcon() {
        val icon = if (viewModel.isSortedByDateDescending) {
            R.drawable.ic_filter_alt
        } else {
            R.drawable.ic_filter
        }
        binding.sort.setImageResource(icon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}