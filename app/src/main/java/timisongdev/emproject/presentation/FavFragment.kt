package timisongdev.emproject.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timisongdev.emproject.databinding.FragmentFavBinding
import timisongdev.emproject.domain.model.Course

class FavFragment : Fragment() {
    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MenuViewModel by activityViewModel()

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
        _binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavorites.adapter = adapter
        binding.recyclerFavorites.clipToPadding = false

        viewModel.courses.observe(viewLifecycleOwner) { allCourses ->
            val favoriteCourses = allCourses.filter { it.hasLike }

            if (favoriteCourses.isEmpty()) {
                binding.title.visibility = View.VISIBLE
                binding.recyclerFavorites.visibility = View.GONE
            } else {
                binding.title.visibility = View.GONE
                binding.recyclerFavorites.visibility = View.VISIBLE

                adapter.items = favoriteCourses
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCourses()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}