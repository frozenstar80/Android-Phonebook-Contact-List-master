package com.example.user.android.view

import com.example.user.android.viewmodel.ContactViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.user.android.model.Contact
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.user.android.R
import com.example.user.android.databinding.FragmentContactListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ContactListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactListFragment : Fragment(), View.OnClickListener {
    private var contactViewModel: ContactViewModel? = null
    private var binding: FragmentContactListBinding? = null
    private var isSearch = false
    var adapter: ContactAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        contactViewModel = ContactViewModel(requireContext().applicationContext)
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        initRecyclerView()
        binding!!.searchView.setOnClickListener(this)
        binding!!.etSearch.setOnClickListener(this)
        return view
    }

    private fun initRecyclerView() {
        binding!!.contactRecyclerView.layoutManager =
            LinearLayoutManager(binding!!.contactRecyclerView.context)
        binding!!.contactRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding!!.contactRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter = ContactAdapter(binding!!.contactRecyclerView.context)
        adapter!!.setContacts(contactViewModel!!.contacts)
        binding!!.contactRecyclerView.adapter = adapter
        adapter!!.setOnItemClickListener { view: View?, obj: Contact, position: Int ->
            Toast.makeText(
                context, """
     Contact Selected
     ${obj.name}${obj.phoneNumber}
     """.trimIndent(), Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Handle  clicks  on the registered views
     *
     * @param view pressed view
     */
    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.et_search) {
            isSearch = true
            binding!!.searchLayout.visibility = View.INVISIBLE
            binding!!.searchView.visibility = View.VISIBLE
            setUpSearch()
        }
    }

    /**
     * Close Search
     */
    private fun closeSearch() {
        if (isSearch) {
            isSearch = false
            binding!!.searchLayout.visibility = View.VISIBLE
            binding!!.searchView.visibility = View.INVISIBLE
        }
    }

    /**
     * Set up search.
     */
    private fun setUpSearch() {
        binding!!.searchView.isIconified = false
        binding!!.searchView.queryHint = "Enter name"
        binding!!.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return true
            }
        })
        binding!!.searchView.setOnCloseListener {
            closeSearch()
            true
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ContactListFragment.
         */
        @JvmStatic
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }
}