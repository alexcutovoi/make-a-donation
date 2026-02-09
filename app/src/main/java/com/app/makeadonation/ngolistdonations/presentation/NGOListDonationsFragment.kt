package com.app.makeadonation.ngolistdonations.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.BaseFragment
import com.app.makeadonation.common.Utils
import com.app.makeadonation.common.observe
import com.app.makeadonation.databinding.FragmentNgoListDonationsBinding
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class NGOListDonationsFragment : BaseFragment<FragmentNgoListDonationsBinding>() {
    override val bindingFactory: (LayoutInflater) -> FragmentNgoListDonationsBinding = FragmentNgoListDonationsBinding::inflate
    private val ngoListDonationsViewModel: NGOListDonationsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ngoListDonationsViewModel.run {
            observe(ngoListDonationsChannel.receiveAsFlow(), ::handleEvents)
            init()
        }

        actionBar?.title = getString(R.string.donations_made)
    }

    private fun handleEvents(event: BaseEvent) {
        when (event) {
            is NGOListDonationsEvent.ListDonations -> {
                startSDK(event.uri)
            }
            is NGOListDonationsEvent.CancelDonation -> {
                startSDK(event.uri)
            }
            is NGOListDonationsEvent.CancelledDonation -> {
                ngoListDonationsViewModel.listDonations()
                Utils.showDialog(
                    requireActivity(),
                    event.title,
                    event.description,
                    Pair( getString(R.string.ok), {})
                )
            }
            is NGOListDonationsEvent.ListOrdersSuccess -> {
                binding.ngoRecyclerView.run {
                    adapter =
                        NgoListDonationsAdapter(event.listDonations) { id, ngoDonationInfo ->
                            ngoListDonationsViewModel.cancelOrder(id, ngoDonationInfo)
                        }
                    layoutManager = LinearLayoutManager(
                        requireContext(), LinearLayoutManager.VERTICAL, false
                    )
                }
            }
            is NGOListDonationsEvent.PaymentCancelled -> {
                Utils.showDialog(
                    requireActivity(),
                    event.title,
                    event.description,
                    Pair( getString(R.string.ok), {})
                )
            }
            is NGOListDonationsEvent.PaymentError -> {
                Utils.showDialog(
                    requireActivity(),
                    event.title,
                    event.description,
                    Pair( getString(R.string.ok), {})
                )
            }
            is BaseEvent.ShowLoading -> {
                binding.run {
                    ngoInstitutiionsConstainer.isVisible = false
                    loading.root.isVisible = true
                }
            }
            is BaseEvent.HideLoading -> {
                binding.run {
                    ngoInstitutiionsConstainer.isVisible = true
                    loading.root.isVisible = false
                }
            }
            else -> {}
        }
    }
}
