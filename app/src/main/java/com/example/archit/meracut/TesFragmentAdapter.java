package com.example.archit.meracut;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

/**
 * Created by Archit on 13-03-2016.
 */class TestFragmentAdapter extends FragmentPagerAdapter {
    protected static String[] CONTENT = {"a", "b", "c", "d"};
    static int flag = 0;

    private int mCount = 4;

    public TestFragmentAdapter(FragmentManager fm, String url[]) {
        super(fm);
        this.CONTENT = url;
        flag = 1;
    }
    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
        flag = 0;

    }

    @Override
    public Fragment getItem(int position) {
        return TestFragment.newInstance(CONTENT[position % CONTENT.length], position);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TestFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

    public static final class TestFragment extends Fragment {
        private static final String KEY_CONTENT = "TestFragment:Content";

        public static TestFragment newInstance(String content, int pos) {
            TestFragment fragment = new TestFragment();

            fragment.mContent = content;
            fragment.position = pos;
            return fragment;
        }
        private int position = 0;
        private String mContent = "???";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            ImageView iv = new ImageView(getActivity());
            iv.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            if (position == 0) {
                int x = R.drawable.img1;
                if (flag!=0){
                    /*new DownloadImage(iv)
                            .execute(CONTENT[0]);*/

                    Picasso.with(getActivity())
                            .load(CONTENT[0])
                            .into(iv);
                }
                else {
                    Picasso.with(getActivity())
                            .load("http://www.meracut.com/app/images/offer1.jpg")
                            .into(iv);
                }
            } else if (position == 1)
            {
                if (flag!=0){
                    Picasso.with(getActivity())
                            .load(CONTENT[1])
                            .into(iv);
                }
                else {
                    Picasso.with(getActivity())
                            .load("http://www.meracut.com/app/images/offer2.jpg")
                            .into(iv);
                }
            } else if (position == 2){
                int x = R.drawable.img2;
                if (flag!=0){
                    Picasso.with(getActivity())
                            .load(CONTENT[2])
                            .into(iv);
                }
                else {
                    Picasso.with(getActivity())
                            .load("http://www.meracut.com/app/images/offer3.jpg")
                            .into(iv);
                }
            } else if (position == 3){
                if (flag!=0){
                    Picasso.with(getActivity())
                            .load(CONTENT[3])
                            .into(iv);
                }
                else {
                    Picasso.with(getActivity())
                            .load("http://www.meracut.com/app/images/offer4.jpg")
                            .into(iv);
                }
            }
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            layout.setGravity(Gravity.CENTER);
            layout.addView(iv);

            return layout;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }
    }
}
