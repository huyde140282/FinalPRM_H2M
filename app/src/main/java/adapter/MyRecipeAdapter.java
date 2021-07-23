package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.smallacademy.authenticatorapp.R;

import java.util.ArrayList;
import java.util.List;

import activity.ItemDetail;
import model.Food;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.FoodViewHoler> {

    private List<Food> mFoods;
    private Context context;
    private List<Food> mFoodFull;

    public List<Food> getmFoods() {
        return mFoods;
    }

    public List<Food> getmFoodFull() {
        return mFoodFull;
    }

    public MyRecipeAdapter(Context context) {
        this.context = context;
    }

    public MyRecipeAdapter(List<Food> mFoods, Context context) {
        this.mFoodFull = new ArrayList<>(mFoods);
        this.mFoods = mFoods;
        this.context = context;
    }

    public void setData(List<Food> list) {
        this.mFoods = list;
        notifyDataSetChanged();
    }

    public void filterFoodList(ArrayList<Food> filteredList) {
        mFoods = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyRecipeAdapter.FoodViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_recipe, parent, false);
        return new MyRecipeAdapter.FoodViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeAdapter.FoodViewHoler holder, int position) {
        Food food = mFoods.get(position);
        if (food == null) {
            return;
        }
        String data = food.getImagePath();

        Picasso.get()
                .load(data)
                .fit()
                .centerCrop()
                .into(holder.imgFood);
        holder.txtFood.setText(food.getFoodName());
//        holder.txtCarb.setText(food.getCarb()+"g");
//        holder.txtFat.setText(food.getFat()+"g");
//        holder.txtCalo.setText(food.getCalories()+"g");
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mFoods != null) {
            return mFoods.size();
        }
        return 0;
    }


    public class FoodViewHoler extends RecyclerView.ViewHolder {
        private ImageView imgFood;
        private TextView txtFood;
        private TextView txtCarb;
        private TextView txtCalo;
        private TextView txtFat;

        RelativeLayout layoutForeGround;
        RelativeLayout deleteBackGround;
        RelativeLayout rightBackGround;


        public FoodViewHoler(@NonNull View itemView) {
            super(itemView);
            deleteBackGround = itemView.findViewById(R.id.swipe_delete_background);
            rightBackGround = itemView.findViewById(R.id.swipe_share_background);
            layoutForeGround = itemView.findViewById(R.id.foreground);
            imgFood = itemView.findViewById(R.id.image_cardview_offline);
            txtFood = itemView.findViewById(R.id.text_cardview_offline);
            txtCarb = itemView.findViewById(R.id.txtCarb);
            txtCalo = itemView.findViewById(R.id.txtCalo);
            txtFat = itemView.findViewById(R.id.txtFat);
        }
    }

}
