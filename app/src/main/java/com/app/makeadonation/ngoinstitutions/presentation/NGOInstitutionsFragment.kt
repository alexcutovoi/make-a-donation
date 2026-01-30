package com.app.makeadonation.ngoinstitutions.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.BaseFragment
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
    }

    private fun handleEvents(event: BaseEvent) {
        when (event) {
            is NGOInstitutionsEvent.Institutions -> {
                binding.ngoRecyclerView.run {
                    adapter =
                        NgoInstitutionsAdapter(event.institutions) { ngoInfo, value ->
                            ngoInstitutionsViewModel.donate(value)
                        }
                    layoutManager = LinearLayoutManager(
                        requireContext(), LinearLayoutManager.VERTICAL, false
                    )
                }
            }
            is NGOInstitutionsEvent.PaymentOrder -> {
                teste(event.uri)
            }
            else -> {}
        }
    }

    private fun teste(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        startActivity(intent)
    }
}
