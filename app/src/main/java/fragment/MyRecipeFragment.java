package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

import adapter.ItemTouchHelperListener;
import adapter.MyRecipeAdapter;
import adapter.RecycleViewItemTouchHelper;
import model.Food;
import model.User;

public class MyRecipeFragment extends Fragment implements ItemTouchHelperListener {
    private RecyclerView rcvMyRecipe;
    private MyRecipeAdapter myRecipeAdapter;
    private View mView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    List<Food> foods;
    String userID;
    DocumentReference documentReference;
    private LinearLayout myRecipeView;
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
        myRecipeView=mView.findViewById(R.id.my_recipe_view);

        rcvMyRecipe = mView.findViewById(R.id.contact_recycleView);
        myRecipeAdapter = new MyRecipeAdapter(mView.getContext());
        foods = new ArrayList<>();
        loadFood(foods);
        ItemTouchHelper.SimpleCallback simpleCallback= new RecycleViewItemTouchHelper(0,ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvMyRecipe);
        return mView;
    }

    public void loadFood(List<Food> foods) {

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> foodFB = (List<Map<String, Object>>) document.get("foods");
                        ArrayList<String> names = new ArrayList<>();
                        for (Map<String, Object> group : foodFB) {
                            int calories = Integer.parseInt(group.get("calories").toString());
                            int carb = Integer.parseInt(group.get("carb").toString());
                            String des = group.get("description").toString();
                            int fat = Integer.parseInt(group.get("fat").toString());
                            String imagePath = (String) group.get("imagePath");
                            String ing = (String) group.get("ingredients");
                            int resId = Integer.parseInt(group.get("resId").toString());
                            String foodName = (String) group.get("foodName");
                            Food food = new Food(resId, foodName, des, ing, imagePath, calories, carb, fat);
                            foods.add(food);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext(), RecyclerView.VERTICAL, false);
                        rcvMyRecipe.setLayoutManager(linearLayoutManager);

                        myRecipeAdapter.setData(foods);
                        rcvMyRecipe.setAdapter(myRecipeAdapter);


                    }
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        userID = fAuth.getCurrentUser().getUid();
            if(viewHolder instanceof MyRecipeAdapter.FoodViewHoler){
                String foodName=foods.get(viewHolder.getAdapterPosition()).getFoodName();

                Food food=foods.get(viewHolder.getAdapterPosition());
                int index= viewHolder.getAdapterPosition();
                //remove
               removeFood(index);
                Snackbar snackbar= Snackbar.make(myRecipeView,foodName+" removed!",Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoFood(food,index);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
    }
    public void removeFood(int index){

        myRecipeAdapter.getmFoods().remove(index);

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        documentReference.update("foods",myRecipeAdapter.getmFoods());

        myRecipeAdapter.notifyItemRemoved(index);
    }
    public void undoFood(Food food,int index){
        myRecipeAdapter.getmFoods().add(index, food);

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        documentReference.update("foods",myRecipeAdapter.getmFoods());

        myRecipeAdapter.notifyItemInserted(index);
    }


}