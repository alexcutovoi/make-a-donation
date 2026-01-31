package com.app.makeadonation.ngoinstitutions.presentation

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.makeadonation.R
import com.app.makeadonation.common.Utils
import com.app.makeadonation.databinding.NgoItemBinding
import com.app.makeadonation.databinding.NgoSelectedInstitutionInfoBinding
import com.app.makeadonation.ngoinstitutions.domain.entity.NgoInfo
import java.util.Locale

class NgoInstitutionsAdapter(
    private val items: List<NgoInfo>,
    private val onClick: (NgoInfo, Long) -> Unit
) : RecyclerView.Adapter<NgoInstitutionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NgoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val ngoItemView: View
    ) : RecyclerView.ViewHolder(ngoItemView) {
        fun bind(item: NgoInfo) {
            val binding = NgoItemBinding.bind(ngoItemView)
            val iconId = ngoItemView.context.run {
                resources.getIdentifier(
                    item.imageLink.lowercase(), "drawable", packageName
                )
            }
            val contentBinding = NgoSelectedInstitutionInfoBinding.inflate(
                LayoutInflater.from(ngoItemView.context), binding.content, true
            )

            binding.run {
                ngoName.text = item.name
                ngoImage.setImageResource(iconId)

                content.isVisible = false

                contentBinding.run {
                    ngoItemAdditionalInfo.run {
                        ngoDataTitle.text = ngoItemView.context.getString(R.string.date_donation_title)
                        ngoData.text = Utils.getCurrentDate()
                    }
                    ngoDescription.run {
                        text = item.info
                        isVisible = item.info.isNotEmpty()
                    }
                    donationValue.run {
                        locale = Locale("pt", "BR")
                        addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(p0: Editable?)  = Unit

                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                donateButton.isEnabled = donationValue.rawValue > 0L
                            }
                        })
                    }
                }
                var isExpanded = content.isVisible

                container.setOnClickListener {
                    isExpanded = !isExpanded

                    contentBinding.run {
                        content.isVisible = isExpanded
                        donateButton.isVisible = isExpanded
                    }

                    val drawable = if(isExpanded) {
                        R.drawable.ic_arrow_up
                    } else {
                        R.drawable.ic_arrow_down
                    }

                    arrow.setImageDrawable(
                        AppCompatResources.getDrawable(root.context,drawable)
                    )
                }

                donateButton.setOnClickListener {
                    onClick(item, contentBinding.donationValue.rawValue)
                }
            }
        }
    }
}
