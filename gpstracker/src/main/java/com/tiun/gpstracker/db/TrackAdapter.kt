package com.tiun.gpstracker.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tiun.gpstracker.R
import com.tiun.gpstracker.databinding.TrackItemBinding

class TrackAdapter(private val listener: Listener) :
    ListAdapter<TrackItem, TrackAdapter.Holder>(Comparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(view: View, private val listener: Listener) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private val binding = TrackItemBinding.bind(view)
        private var trackTemp: TrackItem? = null

        init {
            binding.ibDelete.setOnClickListener(this)
        }

        fun bind(track: TrackItem) = with(binding) {
            trackTemp = track
            tvDate.text = track.date
            tvDistance.text = track.distance
            tvSpeed.text = track.velocity
            tvTime.text = track.time
        }

        override fun onClick(p0: View?) {
            trackTemp?.let {
                listener.onClick(it)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<TrackItem>() {
        override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onClick(track: TrackItem)
    }
}
