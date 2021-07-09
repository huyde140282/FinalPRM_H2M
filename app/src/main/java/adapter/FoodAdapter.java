package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.smallacademy.authenticatorapp.R;


import java.util.List;

import model.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHoler> {

    private List<Food> mFoods;

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
