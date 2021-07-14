package activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import net.smallacademy.authenticatorapp.R;

public class ItemDetail extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView txtInstruction, txtIngredients, txtCalo, txtCarb, txtFat;
    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        Intent intent = getIntent();

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        txtInstruction = findViewById(R.id.food_instruction);
        txtIngredients = findViewById(R.id.ingredient_detail);
        txtCalo = findViewById(R.id.Calories_infor);
        txtCarb = findViewById(R.id.Carb_infor);
        txtFat = findViewById(R.id.Fat_infor);
        imgView = findViewById(R.id.img_slide);

        //Set food image
        int imgFood = intent.getIntExtra("img", 0);
        imgView.setImageResource(imgFood);

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
    }
}
