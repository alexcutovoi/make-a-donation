package com.app.makeadonation.ngocategories.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.BaseFragment
import com.app.makeadonation.common.Utils
import com.app.makeadonation.common.observe
import com.app.makeadonation.databinding.FragmentNgoCategoriesBinding
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class NGOCategoriesFragment : BaseFragment<FragmentNgoCategoriesBinding>() {
    override val bindingFactory: (LayoutInflater) -> FragmentNgoCategoriesBinding = FragmentNgoCategoriesBinding::inflate
    private val ngoCategoriesViewModel: NGOCategoriesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ngoCategoriesViewModel.run {
            observe(ngoCategoriesChannel.receiveAsFlow(), ::handleEvents)
            init()
        }

        actionBar?.title = getString(R.string.app_name)

        setup()
    }

    private fun handleEvents(event: BaseEvent) {
        when (event) {
            is NGOCategoriesEvent.Categories -> {
                binding.categories.adapter =
                    NgoCategoriesAdapter(event.categories) { category ->
                        findNavController().navigate(
                            NGOCategoriesFragmentDirections.actionNgoInstitutions(
                                category.id.toInt(),category.name
                            )
                        )
                    }
            }
            is NGOCategoriesEvent.EmptyCategories -> {
                Utils.showDialog(
                    requireContext(), 
                    event.title, 
                    event.description,
                    Pair(getString(R.string.try_again), { ngoCategoriesViewModel.init() }),
                    Pair(getString(R.string.exit), { requireActivity().finish() } )
                )
            }
            is BaseEvent.ShowLoading -> {
                binding.run {
                    ngoCategoriesContainer.isVisible = false
                    listDonationButton.isVisible = false
                    loading.root.isVisible = true
                }
            }
            is BaseEvent.HideLoading -> {
                binding.run {
                    ngoCategoriesContainer.isVisible = true
                    listDonationButton.isVisible = true
                    loading.root.isVisible = false
                }
            }
            else -> {}
        }
    }

    private fun setup() {
        binding.listDonationButton.setOnClickListener {
            findNavController().navigate(
                NGOCategoriesFragmentDirections.actionNgoListDonations()
            )
        }
    }
}