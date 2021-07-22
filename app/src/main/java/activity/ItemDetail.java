package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import net.smallacademy.authenticatorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Food;

public class ItemDetail extends AppCompatActivity {
    public static final String TAG = "TAG";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView txtInstruction, txtIngredients, txtCalo, txtCarb, txtFat;
    private ImageView imgView;
    private Button addFood;
    private String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    List<Food> foods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        txtInstruction = findViewById(R.id.food_instruction);
        txtIngredients = findViewById(R.id.ingredient_detail);
        txtCalo = findViewById(R.id.Calories_infor);
        txtCarb = findViewById(R.id.Carb_infor);
        txtFat = findViewById(R.id.Fat_infor);
        imgView = findViewById(R.id.img_slide);
        addFood = findViewById(R.id.add_btn);
        //Set food image
        String imgFood = intent.getStringExtra("img");
        Picasso.get()
                .load(imgFood)
                .fit()
                .centerCrop()
                .into(imgView);

        //Set food name
        String foodName = intent.getStringExtra("foodname");
        collapsingToolbarLayout.setTitle(foodName);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Widget_AppCompat_Light_ActionBar_Solid);

        //Set food instruction
        String instruction = intent.getStringExtra("description");
        txtInstruction.setText(instruction);

        //Set food ingredients
        String ingredient = intent.getStringExtra("ingredients");
        txtIngredients.setText(ingredient.replaceAll("     ","\n"));

        //Set food nutrition
        int calo = intent.getIntExtra("calo",1);
        int carb = intent.getIntExtra("carb",2);
        int fat = intent.getIntExtra("fat",3);
        //Set food resId
        int resId= intent.getIntExtra("resId",4);
        txtCalo.setText(calo+"g");
        txtCarb.setText(carb+"g");
        txtFat.setText(fat+"g");


        foods = new ArrayList<>();

        Food favoriteFood = new Food(resId, foodName, instruction, ingredient, imgFood, calo, carb, fat);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<Map<String, Object>> foodFB = (List<Map<String, Object>>) document.get("foods");
                                ArrayList<Food> foods = new ArrayList<>();
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
                                foods.add(favoriteFood);
                                documentReference.update("foods",foods);
                            }
                        }
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }

                });
            }
        });

    }
    public boolean checkFoodList(List<Food> foods,Food favoriteFood)
    {
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> foodFB = (List<Map<String, Object>>) document.get("foods");
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

                    }
                }
            }
        });
        return true;
    }
}
