package fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import net.smallacademy.authenticatorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.MyRecipeAdapter;
import model.Food;

public class MyRecipeFragment extends Fragment {
    private RecyclerView rcvMyRecipe;
    private MyRecipeAdapter myRecipeAdapter;
    private View mView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    List<Food> foods;
    String userID;
    public static final String TAG = "TAG";
    public MyRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_recipe_list, container, false);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        rcvMyRecipe = mView.findViewById(R.id.contact_recycleView);
        myRecipeAdapter = new MyRecipeAdapter(mView.getContext());
         loadFood();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext(), RecyclerView.VERTICAL, false);
//        rcvMyRecipe.setLayoutManager(linearLayoutManager);
////        View Category;
//
//        myRecipeAdapter.setData(loadFood());
//        rcvMyRecipe.setAdapter(myRecipeAdapter);
//        EventChangeListener();
        return mView;
    }
    public void loadFood()
    {
        foods = new ArrayList<>();
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> foodFB = (List<Map<String, Object>>) document.get("foods");
                        ArrayList<String> names = new ArrayList<>();
                        for (Map<String, Object> group : foodFB) {
//                            String name =  group.get("description").toString();
                            int calories = Integer.parseInt(group.get("calories").toString()) ;
                            int carb = Integer.parseInt(group.get("carb").toString());
                            String des= group.get("description").toString();
                             int fat= Integer.parseInt(group.get("fat").toString()) ;
                             String imagePath=(String) group.get("imagePath");
                             String ing=(String) group.get("ingredients");
                            int resId= Integer.parseInt(group.get("resId").toString()) ;
                            String foodName=(String) group.get("foodName");
                            Food food=new Food(resId,foodName,des,ing,imagePath,calories,carb,fat);
                            foods.add(food);
//                            Log.d(TAG, "calories"+des+ "Calories :/n"+calories+fat+carb+imagePath+ing+resId+foodName);
                        }
                        Log.d(TAG, String.valueOf(foods.size()));
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext(), RecyclerView.VERTICAL, false);
                        rcvMyRecipe.setLayoutManager(linearLayoutManager);
//        View Category;

                        myRecipeAdapter.setData(foods);
                        rcvMyRecipe.setAdapter(myRecipeAdapter);
                    }
                }
            }
        });


    }


    private void EventChangeListener() {
        CollectionReference foodRef = fStore.collection("users").document(user.getUid()).collection("foods");
        foodRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Log.e("FireStore error",error.getMessage());
                    return;
                }
                for(DocumentChange dc :value.getDocumentChanges())
                {
                    if(dc.getType()==DocumentChange.Type.ADDED)
                    {
                        foods.add(dc.getDocument().toObject(Food.class));

                    }
                    myRecipeAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void reloadData(){}
}