package com.app.makeadonation.ngolistdonations.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
            is NGOListDonationsEvent.ListOrdersSuccess -> {
                binding.ngoRecyclerView.run {
                    adapter =
                        NgoListDonationsAdapter(event.listOrders.orders) { ngoInfo ->
                           Log.i("INFO", "${ngoInfo.paidAmount}")
                        }
                    layoutManager = LinearLayoutManager(
                        requireContext(), LinearLayoutManager.VERTICAL, false
                    )
                }
            }
            is NGOListDonationsEvent.PaymentCancelled -> {
                Utils.showDialog(requireActivity(), event.title, event.description)
            }
            is NGOListDonationsEvent.PaymentError -> {
                Utils.showDialog(requireActivity(), event.title, event.description)
            }
            else -> {}
        }
    }
}
