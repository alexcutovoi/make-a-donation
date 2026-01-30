package com.app.makeadonation.ngocategories.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.makeadonation.databinding.NgoCategoryItemBinding
import com.app.makeadonation.ngocategories.domain.entity.NgoCategory

class NgoCategoriesAdapter(
    private val items: List<NgoCategory>,
    private val onClick: (NgoCategory) -> Unit
) : RecyclerView.Adapter<NgoCategoriesAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: NgoCategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NgoCategory) {
            val iconId = binding.root.context.run {
                resources.getIdentifier(
                    item.imageLink.lowercase(), "drawable", packageName
                )
            }

            binding.ngoCategoryName.text = item.name
            binding.ngoCategoryImage.setImageResource(iconId)

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NgoCategoryItemBinding.inflate(
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
}
