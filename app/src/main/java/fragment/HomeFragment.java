package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import net.smallacademy.authenticatorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.CategoryAdapter;
import adapter.HomeSlidePhotoAdapter;
import me.relex.circleindicator.CircleIndicator;
import model.Category;
import model.Food;
import model.HomeSlidePhoto;

public class HomeFragment extends Fragment {
    private final String TAG = "TAG";
    StorageReference storageReference;
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private HomeSlidePhotoAdapter homeSlidePhotoAdapter;
    private List<HomeSlidePhoto> mlistSlidePhoto;
    private Timer timer;
    private View mView;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Category> listCategory;
    List<Food> listFood;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
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
//        View Category;
        listFood = new ArrayList<>();
        listCategory = new ArrayList<>();

        categoryAdapter.setData(listCategory);
        rcvCategory.setAdapter(categoryAdapter);

        EventChangeListener();
        //Load Image
        autoSlideImage();

        return mView;
    }

    private void loadfoodtoDB() {
        List<Food> foods = new ArrayList<>();
        //add Food to database
        foods.add(new Food("Melt-in-Your-Mouth Chuck Roast", "abc"));
        foods.add(new Food("Beef and Mushrooms with Smashed Potatoes", "abc"));
        foods.add(new Food("Best-Ever Fried Chicken", "abc"));
        foods.add(new Food("Cheesy Ham Chowder", "abc"));
        foods.add(new Food("Favorite Chicken Potpie", "abc"));
        foods.add(new Food("Honey Chipotle Ribs", "abc"));
        foods.add(new Food("Italian Spiral Meat Loaf", "abc"));
        foods.add(new Food("Potluck Macaroni and Cheese", "abc"));
        foods.add(new Food("Slow Cooker Beef Tips", "abc"));
        foods.add(new Food("Spaghetti Pie Casserole", "abc"));
        foods.add(new Food("Easy Seafood Salad", "abc"));
        foods.add(new Food("Watermelon and Blackberry Sangria", "abc"));
        foods.add(new Food("Rustic Tomato Pie", "abc"));
        foods.add(new Food("Caprese Salad", "abc"));
        foods.add(new Food("Sunday Roast Chicken", "abc"));
        foods.add(new Food("SM Creamy Ranchified Potatoes", "abc"));
        foods.add(new Food("SM Mom's Chopped Coleslaw", "abc"));
        foods.add(new Food("SM Slow-Cooker Potluck Beans", "abc"));
        foods.add(new Food("SM Slow Cooker Beef Tips", "abc"));
        for (Food item : foods) {
            db.collection("foods").add(item);
        }
    }


    private void EventChangeListener() {
        List<Food> food1 = new ArrayList<>();
        List<Food> food2 = new ArrayList<>();
        List<Food> food3 = new ArrayList<>();
        db.collection("foods").orderBy("resId").whereGreaterThan("resId", 4).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FireStore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        food1.add(dc.getDocument().toObject(Food.class));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
        Category category1 = new Category("Most Popular Recipes", food1);
        listCategory.add(category1);
        db.collection("foods").orderBy("foodName").startAt("SM").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FireStore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        food2.add(dc.getDocument().toObject(Food.class));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
        Category category2 = new Category("Get Ready For Summer", food2);
        listCategory.add(category2);
        db.collection("foods").whereEqualTo("resId",0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FireStore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        food3.add(dc.getDocument().toObject(Food.class));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
        Category category3 = new Category("Newest Recipes", food3);
        listCategory.add(category3);

    }

    private void EventChangeListener1() {

        db.collection("foods").orderBy("foodName").startAt("SM").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FireStore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        listFood.add(dc.getDocument().toObject(Food.class));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
        Category category1 = new Category("Get Ready For Summer", listFood);
        listCategory.add(category1);
    }

    private List<HomeSlidePhoto> getListHomePhoto() {
        List<HomeSlidePhoto> list = new ArrayList<>();
        list.add(new HomeSlidePhoto(R.drawable.homeslide3));
        list.add(new HomeSlidePhoto(R.drawable.homeslide2));
        list.add(new HomeSlidePhoto(R.drawable.homeslide1));
        return list;
    }

    private void autoSlideImage() {
        if (mlistSlidePhoto == null || mlistSlidePhoto.isEmpty() || viewPager == null) {
            return;
        }
        //init timer
        if (timer == null) {
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
                        if (currentItem < totalItem) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void reloadData() {
    }
}