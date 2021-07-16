package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.smallacademy.authenticatorapp.R;

import org.w3c.dom.Document;

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

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
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
    private CollectionReference foodRef = db.collection("categories");
    List<Category> categories;
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
//        View Category;
        categories = new ArrayList<>();
        categoryAdapter.setData(categories);
        rcvCategory.setAdapter(categoryAdapter);
        EventChangeListener();
        //Load Image
        autoSlideImage();


        return mView;
    }

    private void loadfoodtoDB() {
        List<Category> list = new ArrayList<>();
        List<Food> listFood = new ArrayList<>();



        listFood.add(new Food(R.drawable.img1, "Beef and Vegetables", "DirectionsInstructions Checklist\n" +
                "Step 1\n" +
                "Heat vegetable oil in a large wok or skillet over medium-high heat; cook and stir beef until browned, 3 to 4 minutes. Move beef to the side of the wok and add broccoli, bell pepper, carrots, green onion, and garlic to the center of the wok. Cook and stir vegetables for 2 minutes.\n" +
                "\n" +
                "Step 2\n" +
                "Stir beef into vegetables and season with soy sauce and sesame seeds. Continue to cook and stir until vegetables are tender, about 2 more minutes.", "2 tablespoons vegetable oil\n" +
                "1 pound beef sirloin, cut into 2-inch strips\n" +
                "1 ½ cups fresh broccoli florets\n" +
                "1 red bell pepper, cut into matchsticks\n" +
                "2 carrots, thinly sliced\n" +
                "1 green onion, chopped\n" +
                "1 teaspoon minced garlic\n" +
                "2 tablespoons soy sauce\n" +
                "2 tablespoons sesame seeds, toasted", 40, 0, 15));
        listFood.add(new Food(R.drawable.img2, "Cheese Crepe", "Step 1\n" +
                "Add flour, milk, eggs, and melted butter to a blender and process until smooth, 1 to 2 minutes. Set batter aside for at least 20 minutes.\n" +
                "\n" +
                "Step 2\n" +
                "Heat 1 teaspoon butter in a skillet over medium heat. Cook onions, stirring occasionally, until they start to turn golden brown, about 5 minutes. Set aside.\n" +
                "\n" +
                "Step 3\n" +
                "Coat a crepe pan or nonstick skillet over medium heat with about 1 teaspoon of the melted butter. Ladle about 1/4 cup batter into the pan, swirling the pan to thinly coat the entire bottom. Cook until small brown spots appear on the bottom of the crepe, about 2 minutes.\n" +
                "\n" +
                "Step 4\n" +
                "Loosen crepe carefully from the pan using a spatula and gently flip to brown the other side, 1 to 2 minutes more. Slide crepe onto a plate. Repeat with remaining batter, buttering the pan as needed and stacking crepes between pieces of parchment or waxed paper.\n" +
                "\n" +
                "Step 5\n" +
                "Assemble crepes, one at a time, in the still-warm pan; spread some mustard over one half, cover mustard with some ham slices, and top with Gruyere cheese. Fold the other side over the filling, pressing down slightly, and heat briefly just until the cheese starts to melt. Fold crepe in half again. Each finished crepe should end up about 1/4 the size of the original.", "Crepe Batter:\n" +
                "1 ¼ cups all-purpose flour\n" +
                "1 cup milk\n" +
                "4 large eggs\n" +
                "1 tablespoon melted butter\n" +
                "Filling:\n" +
                "1 teaspoon butter\n" +
                "1 medium onion, thinly sliced\n" +
                "2 teaspoons butter, divided, or more as needed\n" +
                "2 tablespoons Dijon mustard\n" +
                "½ slice thinly sliced ham\n" +
                "8 ounces shredded Gruyere cheese", 50, 10, 60));
        listFood.add(new Food(R.drawable.img3, "Collet Rice", "Step 1:\n" +
                "\n" +
                "- You use a meat tenderizer or the back of a large knife to soften and flatten the meat so that the meat is about 5-7mm thick.\n" +
                "\n" +
                "Step 2:\n" +
                "\n" +
                "- Sprinkle salt and pepper evenly on one side of the meat.\n" +
                "Step 3:\n" +
                "\n" +
                "- Cover both sides with a thin layer of flour and dip them into the beaten egg. Then, dip the meat into a bowl of fried flour and then put the meat in the refrigerator.", "medium-sized cabbage, finely chopped, rinsed with clean water, drained and refrigerated\n" +
                "\n" +
                "- 1 cup mayonnaise\n" +
                "\n" +
                "- 1-2 tablespoons fresh lemon juice\n" +
                "\n" +
                "- 1 tbsp of honey or sugar\n" +
                "\n" +
                "- 1 tbsp of ketchup tomato sauce", 40, 50, 20));
        listFood.add(new Food(R.drawable.img4, "Egg Cake"));
        listFood.add(new Food(R.drawable.img5, "Vetarian Meal", "In a medium bowl, mash together the avocados, lime juice, and salt. Mix in onion, cilantro, tomatoes, and garlic. Stir in cayenne pepper. Refrigerate 1 hour for best flavor, or serve immediately.", "3 avocados - peeled, pitted, and mashed\n" +
                "1 lime, juiced\n" +
                "1 teaspoon salt\n" +
                "½ cup diced onion\n" +
                "3 tablespoons chopped fresh cilantro\n" +
                "2 roma (plum) tomatoes, diced\n" +
                "1 teaspoon minced garlic\n" +
                "1 pinch ground cayenne pepper (Optional)", 10, 30, 20));
        listFood.add(new Food(R.drawable.img6, "Cheese Rolls", "Step 1\n" +
                "5 Min\n" +
                "Combine first three ingredients in a medium sized saucepan. Cook over low heat until cheese melts, stirring constantly. Allow to cool.\n" +
                "Step 2\n" +
                "2 Min\n" +
                "Spread cheese mixture over bread slices. Roll up and if desired brush with melted butter.\n" +
                "Step 3\n" +
                "3 Min\n" +
                "Place under a preheated grill for 2-3 minutes each side or until golden brown.", "1\n" +
                " \n" +
                "Can\n" +
                "  ( 375 ml )\n" +
                "CARNATION Evaporated Milk\n" +
                "1\n" +
                " \n" +
                "pkt\n" +
                "MAGGI Onion Soup\n" +
                "180\n" +
                " \n" +
                "g\n" +
                "  ( 1.5 cup )\n" +
                "Grated Tasty Cheese\n" +
                "750\n" +
                " \n" +
                "g\n" +
                "Wholemeal Bread\n" +
                "30\n" +
                " \n" +
                "g\n" +
                "Butter", 20, 30, 40));
        listFood.add(new Food(R.drawable.img7, "Egg-Beef Mixture"));
        listFood.add(new Food(R.drawable.img8, "Fried Rice"));
        listFood.add(new Food(R.drawable.img9, "Touhu Sauce Rice"));
        Category category1= new Category("New meals",listFood.subList(0,4));
        Category category2=new Category("Polular",listFood.subList(5,8));
        foodRef.add(category1);
        foodRef.add(category2);
//        list.add(new Category("New meals", listFood.subList(0,4)));
//        list.add(new Category("Popular", listFood.subList(5,8)));
//        list.add(new Category("Recommend for you", listFood.subList(5,8)));
    }
    public List<Category> loadFood() {


        foodRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Category category = documentSnapshot.toObject(Category.class);
                            String categoryName = category.getNameCategory();
                            List<Food> foodList =category.getFoods();
                            categories.add(new Category(categoryName,foodList));
                        }
                    }
                });
        return categories ;
    }
    private void EventChangeListener()
    {

        foodRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable  QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                 Log.e("FireStore error",error.getMessage());
                 return;
                }
                for(DocumentChange dc :value.getDocumentChanges())
                {
                    if(dc.getType()==DocumentChange.Type.ADDED)
                    {
                        categories.add(dc.getDocument().toObject(Category.class));

                    }
                    categoryAdapter.notifyDataSetChanged();
                }

            }
        });
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