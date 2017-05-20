package se.creotec.chscardbalance2.controller

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.gson.Gson

import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.Dish
import se.creotec.chscardbalance2.model.Restaurant

class FoodDishFragment : Fragment() {
    private var restaurant: Restaurant = Restaurant("")
    private var dishListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            restaurant = Gson().fromJson(it.getString(ARG_RESTAURANT), Restaurant::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dish_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            val layoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = layoutManager
            val dividerLine = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
            recyclerView.addItemDecoration(dividerLine)
            recyclerView.adapter = FoodDishRecyclerViewAdapter(restaurant.dishes, dishListener)
        }
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            dishListener = context as OnListFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        dishListener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Dish)
    }

    companion object {
        private val ARG_RESTAURANT = "dish_restaurant"

        fun newInstance(restaurant: Restaurant): FoodDishFragment {
            val fragment = FoodDishFragment()
            val args = Bundle()
            val restaurantJSON = Gson().toJson(restaurant, Restaurant::class.java)
            args.putString(ARG_RESTAURANT, restaurantJSON)
            fragment.arguments = args
            return fragment
        }
    }
}
