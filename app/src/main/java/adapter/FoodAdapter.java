package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHoler holder, int position) {
        Food food = mFoods.get(position);
        if(food == null){
            return;
        }
        holder.imgFood.setImageResource(food.getResId());
        holder.txtFood.setText(food.getFoodName());

        holder.imgFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetail.class);
                intent.putExtra("foodname", food.getFoodName());
                intent.putExtra("description", food.getDescription());
                intent.putExtra("ingredients", food.getIngredients());
                intent.putExtra("calo", food.getCalories());
                intent.putExtra("carb", food.getCarb());
                intent.putExtra("fat", food.getFat());
                intent.putExtra("img", food.getResId());
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
        private TextView txtFood;

        public FoodViewHoler(@NonNull View itemView) {
            super(itemView);

            imgFood = itemView.findViewById(R.id.img_food_rep);
            txtFood = itemView.findViewById(R.id.txt_foodname);
        }
    }
}
