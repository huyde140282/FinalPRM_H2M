package activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.smallacademy.authenticatorapp.R;

import java.util.EventListener;
import java.util.HashMap;
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
        addFood =findViewById(R.id.add_btn);
        //Set food image
        String imgFood = intent.getStringExtra("img");



        //Set food name
        String foodName = intent.getStringExtra("foodname");
        collapsingToolbarLayout.setTitle(foodName);

        //Set food instruction
        String instruction = intent.getStringExtra("description");
        txtInstruction.setText(instruction);

        //Set food ingredients
        String ingredient = intent.getStringExtra("ingredients");
        txtIngredients.setText(ingredient);

        //Set food nutrition
        String calo = intent.getStringExtra("calo");
        String carb = intent.getStringExtra("carb");
        String fat = intent.getStringExtra("fat");

        txtCalo.setText(calo);
        txtCarb.setText(carb);
        txtFat.setText(fat);
        if(!TextUtils.isEmpty(calo) && !TextUtils.isEmpty(carb) && !TextUtils.isEmpty(fat))
        {
             calo1 =Integer.parseInt(calo);
             carb1 =Integer.parseInt(carb);
             fat1 =Integer.parseInt(fat);
        }
        else{
            calo1=1;
            calo1=2;
            fat1=3;
        }
        Food favoriteFood= new Food(1,foodName,instruction,ingredient,imgFood,calo1,carb1,fat1);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                documentReference.update("favoriteFood",favoriteFood);

//                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: Add Sucess "+ userID);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "onFailure: " + e.toString());
//                    }
//                });
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                finish();;;
            }
        });
    }
}
