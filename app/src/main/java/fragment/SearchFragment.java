package fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import net.smallacademy.authenticatorapp.R;

import java.util.ArrayList;
import java.util.List;

import adapter.MyRecipeAdapter;
import model.Food;

public class SearchFragment extends Fragment {
    private RecyclerView rcvSearchView;
    private MyRecipeAdapter searchAdapter;
    private View mView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    List<Food> foods;
    EditText searchText;
    String userID;
    SearchView searchView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String TAG = "TAG";

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_search, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        foods = new ArrayList<>();
        rcvSearchView = (RecyclerView) mView.findViewById(R.id.search_recycleView);
        searchView = (SearchView) mView.findViewById(R.id.recipe_search_view);
        searchAdapter = new MyRecipeAdapter(foods, mView.getContext());
        rcvSearchView.setAdapter(searchAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Food> filterFoodList = filterFood(newText);
                searchAdapter.filterFoodList((ArrayList<Food>) filterFoodList);
                return false;
            }
        });
        EventChangeListener();
        Log.d("FoodListAdapter", String.valueOf(searchAdapter.getmFoods().size()));
        Log.d("FoodListAdapter", String.valueOf(searchAdapter.getmFoodFull().size()));
        // Inflate the layout for this fragment
        return mView;
    }

    private List<Food> filterFood(String text) {
        String lowerCaseQuery = text.toLowerCase();
        List<Food> filteredFoodList = new ArrayList<>();
        for (Food food : foods) {
            String foodName = food.getFoodName().toLowerCase();
            if (foodName.contains(lowerCaseQuery))
                filteredFoodList.add(food);
        }
        return filteredFoodList;
    }


    private void EventChangeListener() {

        db.collection("foods").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FireStore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        foods.add(dc.getDocument().toObject(Food.class));
                        Log.d("FoodList", String.valueOf(foods.size()));
                    }
                    searchAdapter.notifyDataSetChanged();
                }

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void reloadData() {
    }
}