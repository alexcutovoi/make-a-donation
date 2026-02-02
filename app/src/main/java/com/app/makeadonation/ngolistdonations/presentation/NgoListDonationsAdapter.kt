package com.app.makeadonation.ngolistdonations.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.makeadonation.R
import com.app.makeadonation.common.Utils
import com.app.makeadonation.databinding.NgoDonationDataBinding
import com.app.makeadonation.databinding.NgoItemBinding
import com.app.makeadonation.payment.domain.entity.Payment
import com.app.makeadonation.payment.domain.entity.Success

class NgoListDonationsAdapter(
    private val items: List<Success>,
    private val onClick: (String, Payment) -> Unit
) : RecyclerView.Adapter<NgoListDonationsAdapter.ViewHolder>() {
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
        fun bind(item: Success) {
            val binding = NgoItemBinding.bind(ngoItemView)
            /*val iconId = ngoItemView.context.run {
                resources.getIdentifier(
                    item.imageLink.lowercase(), "drawable", packageName
                )
            }*/
            val contentBinding = NgoDonationDataBinding.inflate(
                LayoutInflater.from(ngoItemView.context), binding.content, true
            )

            binding.run {
                contentBinding.run {
                    ngoDonationDate.run {
                        ngoDataTitle.text = ngoItemView.context.getString(R.string.donation_date_title)
                        ngoData.text = item.createdAt
                    }
                    ngoDonationValue.run {
                        ngoDataTitle.text = ngoItemView.context.getString(R.string.donated_value_title)
                        ngoData.text = Utils.formatCurrency(item.price)
                    }
                    ngoDonationStatus.run {
                        ngoDataTitle.text = ngoItemView.context.getString(R.string.donation_status)
                        ngoData.run {
                            text = item.getStatusDescription()
                            setTextColor(
                                context.getColor(
                                    R.color.red.takeIf {
                                        item.isCancelled()
                                    } ?: R.color.blue
                                )
                            )
                        }
                    }

                    donateButton.run {
                        isEnabled = true
                        text = ngoItemView.context.getString(R.string.cancel_donation)
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

                donateButton.run {
                    isEnabled = item.payments.isNotEmpty() && item.isCancelled().not()
                    setOnClickListener {
                        onClick(item.id,  item.payments.first())
                    }
                }
            }
        }
    }
}
