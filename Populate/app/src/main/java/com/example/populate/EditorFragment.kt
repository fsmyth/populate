package com.example.populate

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.populate.data.*
import com.example.populate.databinding.EditorFragmentBinding

class EditorFragment : Fragment() {

    private lateinit var viewModel: EditorViewModel
    private val args: EditorFragmentArgs by navArgs()
    private  lateinit var binding: EditorFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        //Allow the user to return to the home fragment by pressing the back button or by clicking the check icon that has been displayed.
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
        }
        setHasOptionsMenu(true)

        //If the ID does not exist, display the title "New Note". Otherwise, display "Edit Note"
        requireActivity().title =
            if (args.charId == NEW_CHAR_ID) {
                getString(R.string.new_note)
            } else {
                getString(R.string.edit_note)
            }

        viewModel = ViewModelProvider(this).get(EditorViewModel::class.java)

        //Display empty fields when editing a new note
        binding = EditorFragmentBinding.inflate(inflater, container, false)
        binding.charName.setText("")
        binding.charDesc.setText("")
        binding.charNotes.setText("")

        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        saveAndReturn()
                    }
                }
        )

        //Save each of the fields, as well as the cursor's position in the field so they can be maintained when the screen is rotated.
        viewModel.currentChar.observe(viewLifecycleOwner, Observer {
            val savedString = savedInstanceState?.getString(CHAR_TEXT_KEY)
            val savedString2 = savedInstanceState?.getString(CHAR_TEXT_KEY_2)
            val savedString3 = savedInstanceState?.getString(CHAR_TEXT_KEY_3)
            val cursorPosition = savedInstanceState?.getInt(CURSOR_POSITION_KEY) ?: 0
            binding.charName.setText(savedString ?: it.name)
            binding.charName.setSelection(cursorPosition)
            binding.charDesc.setText(savedString ?: it.desc)
            binding.charDesc.setSelection(cursorPosition)
            binding.charNotes.setText(savedString ?: it.notes)
            binding.charNotes.setSelection(cursorPosition)
        })
        viewModel.getCharById((args.charId))

        return binding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> saveAndReturn()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveAndReturn(): Boolean {

        val imm = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        //Convert the new values to strings and sent them to the view model
        viewModel.currentChar.value?.name = binding.charName.text.toString()
        viewModel.currentChar.value?.desc = binding.charDesc.text.toString()
        viewModel.currentChar.value?.notes = binding.charNotes.text.toString()
        //Run a function to set them in the database
        viewModel.updateChar()

        //After updating the item, return to the previous fragment.
        findNavController().navigateUp()
        return true
    }

    //Save the state of the fields, to maintain them when the screen is rotated.
    override fun onSaveInstanceState(outState: Bundle) {
        with(binding.charName) {
            outState.putString(CHAR_TEXT_KEY, text.toString())
            outState.putInt(CURSOR_POSITION_KEY, selectionStart)
        }
        with(binding.charDesc) {
            outState.putString(CHAR_TEXT_KEY_2, text.toString())
            outState.putInt(CURSOR_POSITION_KEY, selectionStart)
        }
        with(binding.charNotes) {
            outState.putString(CHAR_TEXT_KEY_3, text.toString())
            outState.putInt(CURSOR_POSITION_KEY, selectionStart)
        }
        super.onSaveInstanceState(outState)
    }
}