package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.smallacademy.authenticatorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import activity.HomeActivity;
import adapter.CategoryAdapter;
import adapter.HomeSlidePhotoAdapter;
import me.relex.circleindicator.CircleIndicator;
import model.Category;
import model.Food;
import model.HomeSlidePhoto;

public class HomeFragment extends Fragment {

    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private HomeSlidePhotoAdapter homeSlidePhotoAdapter;
    private List<HomeSlidePhoto> mlistSlidePhoto;
    private Timer timer;
    private View mView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        //set for slide images
        viewPager = mView.findViewById(R.id.home_viewpager);
        circleIndicator = mView.findViewById(R.id.circle_indicator);

        mlistSlidePhoto = getListHomePhoto();
        homeSlidePhotoAdapter = new HomeSlidePhotoAdapter(mView.getContext(), mlistSlidePhoto);
        viewPager.setAdapter(homeSlidePhotoAdapter);

        circleIndicator.setViewPager(viewPager);
        homeSlidePhotoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        //set for listing foods in recycler view
        rcvCategory = mView.findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(mView.getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);

        autoSlideImage();
        return  mView;
    }

    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();
        List<Food> listFood = new ArrayList<>();
        listFood.add(new Food(R.drawable.img1, "Beef and Vegetables"));
        listFood.add(new Food(R.drawable.img2, "Beef and Vegetables"));
        listFood.add(new Food(R.drawable.img3, "Beef and Vegetables"));
        listFood.add(new Food(R.drawable.img4, "Beef and Vegetables"));
        listFood.add(new Food(R.drawable.img5, "Beef and Vegetables"));

        list.add(new Category("New for today", listFood));
        list.add(new Category("Recommended", listFood));
        return list;
    }

    private List<HomeSlidePhoto> getListHomePhoto(){
        List<HomeSlidePhoto> list = new ArrayList<>();
        list.add(new HomeSlidePhoto(R.drawable.homeslide3));
        list.add(new HomeSlidePhoto(R.drawable.homeslide2));
        list.add(new HomeSlidePhoto(R.drawable.homeslide1));
        return list;
    }

    private void autoSlideImage(){
        if(mlistSlidePhoto == null || mlistSlidePhoto.isEmpty() || viewPager == null){
            return;
        }
        //init timer
        if(timer == null){
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mlistSlidePhoto.size() - 1;
                        if(currentItem < totalItem){
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        }else{
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 4500);
    }

    //remove timer
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}