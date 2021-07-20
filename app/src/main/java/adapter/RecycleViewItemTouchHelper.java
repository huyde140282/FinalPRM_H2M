package adapter;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class RecycleViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ItemTouchHelperListener listener;
    public RecycleViewItemTouchHelper(int dragDirs, int swipeDirs,ItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener=listener;
    }

    @Override
    public boolean onMove(@NonNull  RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {
          if(listener!=null)
          {
              listener.onSwiped(viewHolder);
          }
    }

    @Override
    public void onSelectedChanged(@Nullable  RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){
            View foreGroundView=((MyRecipeAdapter.FoodViewHoler) viewHolder).layoutForeGround;
            getDefaultUIUtil().onSelected(foreGroundView);
        }
    }

    @Override
    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView=((MyRecipeAdapter.FoodViewHoler) viewHolder).layoutForeGround;
        getDefaultUIUtil().onDraw(c,recyclerView,foreGroundView,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGroundView=((MyRecipeAdapter.FoodViewHoler) viewHolder).layoutForeGround;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foreGroundView,dX,dY,actionState,isCurrentlyActive);;
    }

    @Override
    public void clearView(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
        View foreGroundView=((MyRecipeAdapter.FoodViewHoler) viewHolder).layoutForeGround;
        getDefaultUIUtil().clearView(foreGroundView);
    }
}
