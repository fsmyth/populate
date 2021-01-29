package com.example.populate

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.populate.data.CharacterEntity
import com.example.populate.data.NEW_CHAR_ID
import com.example.populate.data.SELECTED_CHAR_KEY
import com.example.populate.data.TAG
import com.example.populate.databinding.MainFragmentBinding

class MainFragment : Fragment(),
    CharsListAdapter.ListItemListener{

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: CharsListAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            (activity as AppCompatActivity)
                //Do not show the "Up" button, because this is the home fragment.
                .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        // Allows the fragment to utilize an options menu, which is filled with options later.
        setHasOptionsMenu(true)
        //App Title
        requireActivity().title = getString(R.string.app_name)
            //Binding for this fragment
            binding = MainFragmentBinding.inflate(inflater, container, false)
            // View model maintains the database information for the fragment & communicates between the fragment and the database to pull in data
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Ensure all of the recycler view's list items are the same size, and include a divider between them.
        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        //Pulls the database information to display in a list
        viewModel.charsList?.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, it.toString())
            adapter = CharsListAdapter(it, this@MainFragment)
            //Implements binding for the recyclerview adapter
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)

            val selectedChars = savedInstanceState?.getParcelableArrayList<CharacterEntity>(SELECTED_CHAR_KEY)
            adapter.selectedChars.addAll(selectedChars ?: emptyList())
        })

        //When this FAB is pressed, create a new character entity.
        binding.floatingActionButton.setOnClickListener{
            onItemClick(NEW_CHAR_ID)
        }

        return binding.root
    }

    //Create the options for the options menu mentioned above; if there are list items selected, allow the user to access the delete function,
    // otherwise allow the regular menu to be displayed.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if(this::adapter.isInitialized &&
                    adapter.selectedChars.isNotEmpty()
            ) {
                R.menu.menu_main_selected_items
            } else {
                R.menu.menu_main
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //The user can choose one of the options, which will then run the corresponding function.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_sample_data -> addSampleChars()
            R.id.action_delete -> deleteSelectedChars()
            R.id.action_delete_all -> deleteAllChars()
            else -> super.onOptionsItemSelected(item)
        }
    }
    //This function runs when the user selects one or more list items, and chooses to delete them.
    private fun deleteSelectedChars(): Boolean {
        viewModel.deleteChars(adapter.selectedChars)
        Handler(Looper.getMainLooper()).postDelayed({
            //Here, it tells the adapter to clear the selected items from the database.
            adapter.selectedChars.clear()
            //The below line refreshes the options menu, returning it to the default options.
            requireActivity().invalidateOptionsMenu()
        }, 100)
        return true
    }
    //This function tells the view model to run a function to remove all entries from the database.
    private fun deleteAllChars(): Boolean {
        viewModel.deleteAllChars()
        return true
    }

    //This function tells the VM to run a function to add a selection of premade sample items to the database.
    private fun addSampleChars(): Boolean {
        viewModel.addSampleChars()
        return true
    }

    //This function calls the nav controller, telling it to navigate to the given item by passing in the ID of the clicked item.
    override fun onItemClick(charId: Int) {
        Log.i(TAG, "onItemClick: received character id $charId")
        val action = MainFragmentDirections.actionEditChar(charId)
        findNavController().navigate(action)
    }

    //When the number of items selected is changed, refresh the options menu.
    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }

    //This function remembers which list items were selected to maintain them when the screen is rotated.
    //It does this by adding the selected items to a parcelable arraylist
    override fun onSaveInstanceState(outState: Bundle) {
        if (this::adapter.isInitialized) {
            outState.putParcelableArrayList(SELECTED_CHAR_KEY, adapter.selectedChars)
        }
        super.onSaveInstanceState(outState)
    }

}