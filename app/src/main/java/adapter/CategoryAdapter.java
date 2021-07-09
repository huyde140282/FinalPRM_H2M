package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.smallacademy.authenticatorapp.R;

import java.util.List;

import model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> mCategories;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public  void setData(List<Category> list){
        this.mCategories = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_categories, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = mCategories.get(position);
        if(category == null){
            return;
        }
        holder.txtNameCategory.setText(category.getNameCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        holder.rcvFood.setLayoutManager(linearLayoutManager);

        FoodAdapter foodAdapter = new FoodAdapter();
        foodAdapter.setData(category.getFoods());
        holder.rcvFood.setAdapter(foodAdapter);
    }

    @Override
    public int getItemCount() {
        if(mCategories != null){
            return mCategories.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNameCategory;
        private RecyclerView rcvFood;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameCategory = itemView.findViewById(R.id.txt_cat_name);
            rcvFood = itemView.findViewById(R.id.rcv_food);
        }
    }
}
