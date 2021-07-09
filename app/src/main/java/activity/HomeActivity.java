package activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import net.smallacademy.authenticatorapp.R;

import adapter.HomeViewPagerAdapter;


public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager = findViewById(R.id.viewpage);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: bottomNavigationView.getMenu().findItem(R.id.home_tab).setChecked(true);
                            break;
                    case 1: bottomNavigationView.getMenu().findItem(R.id.my_recipe_tab).setChecked(true);
                        break;
                    case 2: bottomNavigationView.getMenu().findItem(R.id.camera_tab).setChecked(true);
                        break;
                    case 3: bottomNavigationView.getMenu().findItem(R.id.profile_tab).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_tab: mViewPager.setCurrentItem(0); break;
                    case R.id.my_recipe_tab: mViewPager.setCurrentItem(1); break;
                    case R.id.camera_tab: mViewPager.setCurrentItem(2); break;
                    case R.id.profile_tab: mViewPager.setCurrentItem(3); break;
                }
                return false;
            }
        });
    }
}
