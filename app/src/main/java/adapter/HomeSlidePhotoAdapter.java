package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import net.smallacademy.authenticatorapp.R;

import java.util.List;

import model.HomeSlidePhoto;

public class HomeSlidePhotoAdapter extends PagerAdapter {

    private Context mContext;
    private List<HomeSlidePhoto> mListPhoto;

    public HomeSlidePhotoAdapter(Context mContext, List<HomeSlidePhoto> mListPhoto) {
        this.mContext = mContext;
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo, container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_slide);
        HomeSlidePhoto hsPhoto = mListPhoto.get(position);
        if(hsPhoto != null){
            Glide.with(mContext).load(hsPhoto.getSlideRedId()).into(imgPhoto);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
