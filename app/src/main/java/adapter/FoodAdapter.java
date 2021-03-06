package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.smallacademy.authenticatorapp.R;


import java.util.List;

import activity.ItemDetail;
import model.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHoler> {

    private List<Food> mFoods;
    private Context context;

    public FoodAdapter(Context context) {
        this.context = context;
    }

    public FoodAdapter(List<Food> mFoods, Context context) {
        this.mFoods = mFoods;
        this.context = context;
    }

    public void setData(List<Food> list){
        this.mFoods = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_horizontal, parent, false);
        return new FoodViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHoler holder, int position) {
        Food food = mFoods.get(position);
        if(food == null){
            return;
        }
        String data=food.getImagePath();

        Picasso.get()
                .load(data)
                .fit()
                .centerCrop()
                .into(holder.imgFood);
        holder.txtFood.setText(food.getFoodName());
        holder.ratingBar.setRating(food.getResId());
        holder.txt_calo.setText(food.getCalories() + "g");
        holder.txt_carb.setText(food.getCarb() + "g");
        holder.txt_fat.setText(food.getFat() + "g");
        holder.imgFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetail.class);
                intent.putExtra("foodName", food.getFoodName());
                intent.putExtra("description", food.getDescription());
                intent.putExtra("ingredients", food.getIngredients());
                intent.putExtra("calo", food.getCalories());
                intent.putExtra("carb", food.getCarb());
                intent.putExtra("fat", food.getFat());
                intent.putExtra("img", food.getImagePath());
                intent.putExtra("resId",food.getResId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mFoods != null){
            return mFoods.size();
        }
        return 0;
    }

    public class FoodViewHoler extends RecyclerView.ViewHolder{
        private ImageView imgFood;
        private TextView txtFood, txt_calo, txt_carb, txt_fat;
        private RatingBar ratingBar;
        public FoodViewHoler(@NonNull View itemView) {
            super(itemView);

            imgFood = itemView.findViewById(R.id.img_food_rep);
            txtFood = itemView.findViewById(R.id.txt_foodname);
            txt_calo = itemView.findViewById(R.id.txt_calo_home);
            txt_carb = itemView.findViewById(R.id.txt_carb_home);
            txt_fat = itemView.findViewById(R.id.txt_fat_home);
            ratingBar = itemView.findViewById(R.id.rating_bar_control);
        }
    }
}
