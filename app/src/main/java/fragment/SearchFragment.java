package fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
    TextView searchText;
    String userID;
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

        rcvSearchView = (RecyclerView) mView.findViewById(R.id.search_recycleView);
        searchAdapter = new MyRecipeAdapter(mView.getContext());
        searchText = mView.findViewById(R.id.recipe_search_text);

        foods = new ArrayList<>();
        searchAdapter.setData(foods);
        rcvSearchView.setAdapter(searchAdapter);


        EventChangeListener();
        // Inflate the layout for this fragment
        return mView;
    }

    private void EventChangeListener() {

        db.collection("foods").orderBy("foodName").startAt(searchText).endAt(searchText + "\uf8ff").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FireStore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        foods.add(dc.getDocument().toObject(Food.class));
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


    public void reloadData() {
    }
}