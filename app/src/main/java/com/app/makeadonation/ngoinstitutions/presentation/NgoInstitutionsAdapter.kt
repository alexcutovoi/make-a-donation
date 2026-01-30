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
import com.app.makeadonation.databinding.NgoItemBinding
import com.app.makeadonation.ngocategories.domain.entity.NgoCategory
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
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: NgoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NgoInfo) {
            val iconId = binding.root.context.run {
                resources.getIdentifier(
                    item.imageLink.lowercase(), "drawable", packageName
                )
            }

            binding.run {
                ngoName.text = item.name
                ngoImage.setImageResource(iconId)
                ngoItemAdditionalInfo.run {
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
                var isExpanded = binding.ngoItemAdditionalInfo.root.isVisible

                binding.run {
                    container.setOnClickListener {
                        isExpanded = !isExpanded

                        binding.ngoItemAdditionalInfo.root.visibility = if(isExpanded){
                            View.VISIBLE
                        } else {
                            View.GONE
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

                    ngoItemAdditionalInfo.run {
                        donateButton.setOnClickListener {
                            onClick(item, donationValue.rawValue)
                        }
                    }
                }

            }
        }
    }
}
