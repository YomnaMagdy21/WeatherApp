package com.example.weatherapp.favorite.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.map.view.MapFragment
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() ,OnFavoriteClickListener{
    lateinit var binding : FragmentFavoriteBinding

    lateinit var favoriteAdapter: FavoriteAdapter
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener{
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.main, MapFragment())
                .addToBackStack(null)
                .commit()
        }
        favoriteViewModelFactory= FavoriteViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(requireContext())
            ))

        favoriteViewModel= ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)

        setUpRecyclerView()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            favoriteViewModel.favorites.collectLatest { result->
                    when(result){
                        is UIState.Loading ->{
//                            binding.progBar.visibility=View.VISIBLE
//                            binding.recView.visibility=View.GONE
                        }
                        is UIState.Success<*> ->{
                           // binding.progBar.visibility=View.GONE
                           // binding.recView.visibility=View.VISIBLE
                            val dataList = result.data as? List<Favorite>
                            favoriteAdapter.submitList(dataList)

                        }
                        else ->{
                          //  binding.progBar.visibility=View.GONE
                            Toast.makeText(requireContext(),"there is problem in server",
                                Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
        }

    fun setUpRecyclerView(){
        favoriteAdapter=FavoriteAdapter(requireContext(),this)
        binding.favRecView.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        }

    }



    override fun onClickToRemove(favorite: Favorite) {
        favoriteViewModel.deleteFavorite(favorite)
    }

    override fun goToDetails(favorite: Favorite) {
        val detailsFragment = FavDetailsFragment.newInstance(favorite)
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main,detailsFragment )
        transaction.addToBackStack(null)
        transaction.commit()
    }
}