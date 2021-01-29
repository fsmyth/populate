package com.example.populate

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.populate.data.CharacterEntity
import com.example.populate.databinding.ListItemBinding

class CharsListAdapter(
    private val charsList: List<CharacterEntity>,
    private val listener: ListItemListener
) :
    RecyclerView.Adapter<CharsListAdapter.ViewHolder>() {

    //Maintain a list of the selected items
    val selectedChars = arrayListOf<CharacterEntity>()

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    //Count the number of items in the list
    override fun getItemCount() = charsList.size

    //Assign each item a value from their position in the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = charsList[position]
        with(holder.binding) {
            //Set the text of the list item field to the "name" field of the database item.
            charName.text = character.name
            //When an item is clicked, send the item id to the OnItemClick function.
            root.setOnClickListener {
                listener.onItemClick(character.id)
            }
            faba.setOnClickListener {
                //If the item is already selected, unselect it and set the icon back to the original.
                if (selectedChars.contains(character)) {
                    selectedChars.remove(character)
                    faba.setImageResource(R.drawable.profile)
                //Otherwise, select the item and set the icon to a check.
                } else {
                    selectedChars.add(character)
                    faba.setImageResource(R.drawable.ic_check)
                }
                //Tell the listener the number of selected items has changed
                listener.onItemSelectionChanged()
            }
            faba.setImageResource(
                if (selectedChars.contains(character)) {
                    R.drawable.ic_check
                } else {
                    R.drawable.profile
                }
            )
        }
    }

    //Listen for the previously mentioned triggers, then run the below functions as directed
    interface ListItemListener {
        fun onItemClick(charId: Int)
        fun onItemSelectionChanged()
    }
}