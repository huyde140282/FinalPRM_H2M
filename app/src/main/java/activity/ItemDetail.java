package activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Timer;
import java.util.TimerTask;

import model.Food;

public class ItemDetail extends AppCompatActivity {
    public static final String TAG = "TAG";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView txtInstruction, txtIngredients, txtCalo, txtCarb, txtFat;
    private ImageView imgView;
    private Button addFood, cancelAdd, btnConfirm;
    private String userID;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private Timer timer, timer2;
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

        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(ItemDetail.this);
        dialog.setContentView(R.layout.add_confirm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(true);


        btnConfirm = dialog.findViewById(R.id.btn_add_confirm);
        cancelAdd = dialog.findViewById(R.id.btn_add_cancel);

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
                dialog.show();

                //Cancel add
                cancelAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                //Confirm add
                btnConfirm.setOnClickListener(new View.OnClickListener() {
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
                                dialog.dismiss();
                                dialog.setContentView(R.layout.add_successfull_dialog);
                                dialog.show();
//                                progressDialog.show();
//                                progressDialog.setContentView(R.layout.progress_round_dialog);
//                                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
//                                        dialog.setContentView(R.layout.add_successfull_dialog);
//                                        dialog.show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    }
                                },3000);
                            }
                        });
                    }
                });

            }
        });

    }
}
