package activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import net.smallacademy.authenticatorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Category;
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
    int calo1;
    int carb1;
    int fat1;

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
        txtIngredients.setText(ingredient);

        //Set food nutrition
        int calo = intent.getIntExtra("calo",1);
        int carb = intent.getIntExtra("carb",2);
        int fat = intent.getIntExtra("fat",3);

        txtCalo.setText(calo+"g");
        txtCarb.setText(carb+"g");
        txtFat.setText(fat+"g");
//        if (!TextUtils.isEmpty(calo) && !TextUtils.isEmpty(carb) && !TextUtils.isEmpty(fat)) {
//            calo1 = Integer.parseInt(calo);
//            carb1 = Integer.parseInt(carb);
//            fat1 = Integer.parseInt(fat);
//        } else {
//            calo1 = 1;
//            carb1 = 2;
//            fat1 = 3;
//            txtCalo.setText(calo1+"g");
//            txtCarb.setText(carb1+"g");
//            txtFat.setText(fat1+"g");
//        }
        Food favoriteFood = new Food(1, foodName, instruction, ingredient, imgFood, calo, carb, fat);
        List<Food> foods = new ArrayList<>();
        foods.add(favoriteFood);
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
                                ArrayList<String> names = new ArrayList<>();
                                for (Map<String, Object> group : foodFB) {
//                            String name =  group.get("description").toString();
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
}
