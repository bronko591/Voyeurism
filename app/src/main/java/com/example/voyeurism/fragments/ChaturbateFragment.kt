package com.example.voyeurism.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.voyeurism.viewmodels.ChaturbateViewModel
import com.example.voyeurism.R
import com.example.voyeurism.adapters.ChaturbateAdapter
import com.example.voyeurism.models.ChaturbateModel

class ChaturbateFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[ChaturbateViewModel::class.java] }
    private lateinit var loaderTextView: TextView
    private lateinit var loaderProgressBar: ProgressBar
    private lateinit var loaderButton: Button
    private lateinit var adapter: ChaturbateAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private var urlKey = ""
   // m - Male
   // c - Couple
   // f - Female
   // s - Transexual
    companion object {
        fun newInstance(Url: String): ChaturbateFragment {
            return ChaturbateFragment().apply {
                arguments = Bundle().apply { putString("Key", Url) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey("Key"))
                urlKey = requireArguments().getString("Key").toString()
        } else {
            adapter = ChaturbateAdapter(requireContext())
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chaturbate, container, false)
        loaderTextView = view.findViewById(R.id.loader_textView) as TextView
        loaderProgressBar = view.findViewById(R.id.loader_progressBar) as ProgressBar
        loaderButton = view.findViewById(R.id.loader_button) as Button
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh) as SwipeRefreshLayout
        screenRotateWithGridManager()
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        handleFirstLoad(urlKey)
        handleSwipeToRefreshing()
        return view
    }

    private fun handleFirstLoad(genderSelection:String){
        loaderProgressBar.visibility = View.VISIBLE
        viewModel.fetchData(this.requireContext(), genderSelection).observe(viewLifecycleOwner, Observer {
            adapter.addMore(it)
            recyclerView.adapter = adapter
            loaderProgressBar.visibility = View.GONE
        })
        loaderProgressBar.visibility = View.GONE
    }

    private fun handleSwipeToRefreshing(){
        swipeToRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_dark)
        swipeToRefresh.setOnRefreshListener {
            refresh()
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun screenRotateWithGridManager(){
        if (recyclerView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        } else if (recyclerView.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(requireActivity(), 4)
        }
    }

    private fun refresh(){
        val modelsList: MutableList<ChaturbateModel> = mutableListOf()
        recyclerView.adapter.apply {
            modelsList.addAll(adapter.getModels())
            adapter.clearAll()
        }
        adapter.addMore(modelsList)
        recyclerView.adapter = adapter
        writeToast("Refreshed.")
    }

    private fun writeToast(message:String){
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

}