package andrew.com.LetsAct;

import android.content.Context;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;

import java.util.ArrayList;

public class ImageSwitcherHandler{

    private ImageSwitcher mImageSwitcher;
    private Context mContext;

    private static final String TAG = "ImageSwitcherHandler";

    private int imageResourcesIndex = 0;
    private ArrayList<Integer> imageResources = new ArrayList<>();

    public ImageSwitcherHandler(Context context, ImageSwitcher imageSwitcher){

        mImageSwitcher = imageSwitcher;
        mContext = context;

        addImageResources();
    }

    public void initializeOnTouchListener(){
        mImageSwitcher.setOnTouchListener(new OnSwipeTouchListener(mContext){
            public void onSwipeRight(){
                Log.d(TAG, "swipe right");

                rightSwipeAnimation();
                goToNextImage();
            }
            public void onSwipeLeft(){
                Log.d(TAG, "swipe left");

                leftSwipeAnimation();
                goToPreviousImage();
            }
        });
    }

    private void addImageResources(){
        Log.d(TAG, "images added");

        imageResources.add(R.drawable.stjudelogo);
        imageResources.add(R.drawable.neighborhoodofgood);
    }

    private void rightSwipeAnimation(){
        mImageSwitcher.setOutAnimation(AnimationUtils.makeOutAnimation(mContext,true));
        mImageSwitcher.setInAnimation(AnimationUtils.makeInAnimation(mContext,true));
    }

    private void goToNextImage(){
        try {
            imageResourcesIndex++;
            mImageSwitcher.setImageResource(imageResources.get(imageResourcesIndex));
            Log.d(TAG, "image res:" + imageResources.get(imageResourcesIndex));
        }
        catch (IndexOutOfBoundsException e){
            fixImageResourcesIndex();
            mImageSwitcher.setImageResource(imageResources.get(imageResourcesIndex));
            Log.d(TAG, e+", image res:" + imageResources.get(imageResourcesIndex));
        }
    }

    private void leftSwipeAnimation(){
        mImageSwitcher.setOutAnimation(AnimationUtils.makeOutAnimation(mContext,false));
        mImageSwitcher.setInAnimation(AnimationUtils.makeInAnimation(mContext,false));
    }

    private void goToPreviousImage(){
        try {
            imageResourcesIndex--;
            mImageSwitcher.setImageResource(imageResources.get(imageResourcesIndex));
            Log.d(TAG, "image res:" + imageResources.get(imageResourcesIndex));
        }
        catch(IndexOutOfBoundsException e){
            fixImageResourcesIndex();
            mImageSwitcher.setImageResource(imageResources.get(imageResourcesIndex));
            Log.d(TAG, e+", image res:" + imageResourcesIndex);
        }
    }

    private void fixImageResourcesIndex(){
        if (imageResourcesIndex == imageResources.size()){
            imageResourcesIndex = 0;
        }
        else if (imageResourcesIndex == -1){
            imageResourcesIndex = imageResources.size()-1;
        }
    }
}
