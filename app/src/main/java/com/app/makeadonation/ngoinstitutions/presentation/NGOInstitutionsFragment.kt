package com.app.makeadonation.ngoinstitutions.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.BaseFragment
import com.app.makeadonation.common.Utils
import com.app.makeadonation.common.observe
import com.app.makeadonation.databinding.FragmentNgoInstitutionsBinding
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class NGOInstitutionsFragment : BaseFragment<FragmentNgoInstitutionsBinding>() {
    override val bindingFactory: (LayoutInflater) -> FragmentNgoInstitutionsBinding = FragmentNgoInstitutionsBinding::inflate
    private val ngoInstitutionsViewModel: NGOInstitutionsViewModel by viewModel()
    private val args: NGOInstitutionsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ngoInstitutionsViewModel.run {
            observe(ngoInstitutionsChannel.receiveAsFlow(), ::handleEvents)
            init(args.ngoCategoryId)
        }

        actionBar?.title = args.ngoCategoryName
    }

    private fun handleEvents(event: BaseEvent) {
        when (event) {
            is NGOInstitutionsEvent.Institutions -> {
                binding.ngoRecyclerView.run {
                    adapter =
                        NgoInstitutionsAdapter(event.institutions) { ngoInfo, value ->
                            ngoInstitutionsViewModel.donate(ngoInfo, value)
                        }
                    layoutManager = LinearLayoutManager(
                        requireContext(), LinearLayoutManager.VERTICAL, false
                    )
                }
            }
            is NGOInstitutionsEvent.PaymentOrder -> {
                startSDK(event.uri)
            }
            is NGOInstitutionsEvent.PaymentSuccess -> {
                findNavController().navigate(
                    NGOInstitutionsFragmentDirections.actionNgoDonationCnfirmation(
                        event.selectedNgo.name, event.donationValue
                    )
                )
            }
            is NGOInstitutionsEvent.PaymentCancelled -> {
                Utils.showDialog(requireActivity(), event.title, event.description)
            }
            is NGOInstitutionsEvent.PaymentError -> {
                Utils.showDialog(requireActivity(), event.title, event.description)
            }
            is BaseEvent.ShowLoading -> {
                binding.run {
                    ngoInstitutionsContainer.isVisible = false
                    loading.root.isVisible = true
                }
            }
            is BaseEvent.HideLoading -> {
                binding.run {
                    ngoInstitutionsContainer.isVisible = true
                    loading.root.isVisible = false
                }
            }
            else -> {}
        }
    }
}
