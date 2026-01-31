package com.app.makeadonation.ngodonationreceipt.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.BaseFragment
import com.app.makeadonation.common.observe
import com.app.makeadonation.databinding.FragmentNgoReceiptBinding
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class NGOReceiptFragment : BaseFragment<FragmentNgoReceiptBinding>() {
    override val bindingFactory: (LayoutInflater) -> FragmentNgoReceiptBinding = FragmentNgoReceiptBinding::inflate
    private val ngoReceiptViewModel: NGOReceiptViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(
            ngoReceiptViewModel.ngoReceiptChannel.receiveAsFlow(),
            ::handleEvents
        )

        actionBar?.title = getString(R.string.app_name)

        setup()
    }

    private fun handleEvents(event: BaseEvent) {
        when (event) {
            is NGOReceiptEvent.GoToHome -> {
                findNavController().navigate(
                    R.id.ngoCategoriesFragment,
                    null,
                    navOptions {
                        popUpTo(R.id.ngoCategoriesFragment) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                )
            }
            else -> {}
        }
    }

    private fun setup() {
        binding.homeButton.setOnClickListener {
            ngoReceiptViewModel.goToHome()
        }
    }
}