package com.naumov.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naumov.pictureoftheday.R
import com.naumov.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.naumov.pictureoftheday.repository.PODServerResponseData
import com.naumov.pictureoftheday.ui.MainActivity
import com.naumov.pictureoftheday.utils.toast
import com.naumov.pictureoftheday.viewmodel.PictureOfTheDayData
import com.naumov.pictureoftheday.viewmodel.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getData()
            .observe(viewLifecycleOwner) { renderData(it) }
        viewModel.sendRequestToday()

        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        setBottomSheetBehavior()
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        if (savedInstanceState == null) {
            binding.chipGroup.check(R.id.chip_td)
        }

        setBottomAppBar(view)
        initChip()
    }

    private fun initChip() {

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            for (ch: Int in checkedIds) {
                when (ch) {
                    binding.chipDby.id -> {
                        viewModel.sendRequestTDBY()
                    }
                    binding.chipTd.id -> {
                        viewModel.sendRequestToday()
                    }
                    binding.chipEst.id -> {
                        viewModel.sendRequestYT()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> requireView().toast("Favourite", requireContext())
            R.id.app_bar_settings -> requireView().toast("Settings", requireContext())
//                activity?.supportFragmentManager?.beginTransaction()?.add(R.id.container, ChipsFragment.newInstance())
//                ?.addToBackStack(null)
//                ?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData

                val url = serverResponseData.url

                if (url.isNullOrEmpty()) {
                    requireView().toast("Link is empty", requireContext())
                } else {
                      setData(serverResponseData)
                    binding.includeBottomSheet.bottomSheetDescriptionHeader.text =
                        data.serverResponseData.title
                    binding.includeBottomSheet.bottomSheetDescription.text =
                        data.serverResponseData.explanation
                }
            }
            is PictureOfTheDayData.Loading -> {
                //TODO
            }
            is PictureOfTheDayData.Error -> {
                requireView().toast(data.error.message, requireContext())
            }
        }
    }

    private fun setData(data: PODServerResponseData) {
        val url = data.hdurl
        if (url.isNullOrEmpty()) {
            val videoUrl = data.url
            videoUrl?.let { showAVideoUrl(it) }
        } else {
            binding.imageView.load(url) {
                lifecycle(this@PictureOfTheDayFragment)
                error(R.drawable.ic_load_error_vector)
                placeholder(R.drawable.ic_no_photo_vector)
                crossfade(true)
            }

        }
    }
    private fun showAVideoUrl(videoUrl: String) = with(binding) {
        binding.imageView.visibility = View.GONE
        videoOfTheDay.visibility = View.VISIBLE
        videoOfTheDay.text =  getString(R.string.message_hdurl,videoUrl,">","<")
        videoOfTheDay.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(videoUrl)
            }
            startActivity(i)
        }
    }
    private fun setBottomSheetBehavior() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.includeBottomSheet.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

}