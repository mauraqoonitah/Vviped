package com.ilkom.vviped.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilkom.vviped.R
import com.ilkom.vviped.model.SellingPostItem
import com.ilkom.vviped.model.SellingPostRepository
import com.ilkom.vviped.model.SellingPostsAdapter
import com.ilkom.vviped.model.SellingPostsCategoryAdapter
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var recyclerView: RecyclerView? = null
    private var sellingPostCategoryAdapter: SellingPostsCategoryAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        recyclerView = view.findViewById(R.id.recycleView_category)
        recyclerView?.setHasFixedSize(true)
//        recyclerView?.layoutManager = LinearLayoutManager(context)

        //grid layout
        val manager = GridLayoutManager(context, 2)
        view.recycleView_category.layoutManager = manager

        getDataCategoryImg()
        return view



    }

    private fun getDataCategoryImg() {
        val feedsService = SellingPostRepository.create()
        feedsService.getFeeds().enqueue(object : Callback<MutableList<SellingPostItem>> {
            override fun onResponse(
                call: Call<MutableList<SellingPostItem>>,
                response: Response<MutableList<SellingPostItem>>
            ) {
                sellingPostCategoryAdapter = context?.let { SellingPostsCategoryAdapter(it, response.body() as MutableList<SellingPostItem>) }
                recyclerView?.adapter = sellingPostCategoryAdapter
                sellingPostCategoryAdapter?.notifyDataSetChanged()


                refreshTabHomeCategory()
                if(progressBarTabCategory != null) {
                    progressBarTabCategory.visibility = View.GONE
                } else {
                    progressBarTabCategory.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<MutableList<SellingPostItem>>, t: Throwable) {
                Log.e("tag", t.toString())
            }
        })
    }

    private fun refreshTabHomeCategory() {
        swipeRefreshTabCategory.setOnRefreshListener {
            getDataCategoryImg()
            swipeRefreshTabCategory.isRefreshing = false
        }

    }


    fun View.hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)

    }

}