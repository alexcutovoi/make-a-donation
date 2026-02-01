package com.app.makeadonation.ngodonationconfirmation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.BaseFragment
import com.app.makeadonation.common.observe
import com.app.makeadonation.databinding.FragmentNgoReceiptBinding
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class NGODonationConfirmationFragment : BaseFragment<FragmentNgoReceiptBinding>() {
    override val bindingFactory: (LayoutInflater) -> FragmentNgoReceiptBinding = FragmentNgoReceiptBinding::inflate
    private val ngoDonationConfirmationModel: NGODonationConfirmationModel by viewModel()
    private val args: NGODonationConfirmationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(
            ngoDonationConfirmationModel.ngoReceiptChannel.receiveAsFlow(),
            ::handleEvents
        )

        actionBar?.run {
            title = getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        setup()

        args.run {
            ngoDonationConfirmationModel.showDonation(ngoName, ngoDonationValue)
        }
    }

    private fun handleEvents(event: BaseEvent) {
        when (event) {
            is NGODonationConfirmationEvent.ShowDonationData -> {
                binding.run {
                    description.text = getString(
                        R.string.thanks_donation_description,
                        event.ngoName
                    )

                    donationValue.text = event.donationValue
                }
            }
            is NGODonationConfirmationEvent.GoToHome -> {
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
            ngoDonationConfirmationModel.goToHome()
        }
    }
}