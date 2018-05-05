package bluetooth.bth_se.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import bluetooth.bth_se.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class jay_main extends Fragment {
    public String Tag = "Tab";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View rootView;

    public jay_main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getChildFragmentManager(),getActivity());

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);


        initFrame_img();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFrame_Img(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Log.i(Tag,"Select 2 ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.i(Tag,"Select 3 ");
            }
        });
        viewPager.setCurrentItem(0);
        return rootView;
    }
    LinearLayout lay_record;
    TextView txt_configure,txt_show;int Select_color, Blackground_color;
    private void initFrame_img(){
        txt_configure = (TextView) rootView.findViewById(R.id.txt_configure);
        txt_show = (TextView) rootView.findViewById(R.id.txt_show);
        lay_record = (LinearLayout) rootView.findViewById(R.id.record_lay);

        txt_configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        txt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        Select_color = R.color.enoxs_yellow;
        Blackground_color = R.color.enoxs_pupler;
    }
    private void setFrame_Img(int num) {
        txt_configure.setBackgroundResource(Blackground_color);
        txt_configure.setTextColor(this.getResources().getColor(Select_color));
        txt_show.setBackgroundResource(Blackground_color);
        txt_show.setTextColor(this.getResources().getColor(Select_color));
        switch(num){//txt_configure,txt_show,txt_setting
            case 0:
                txt_configure.setBackgroundResource(Select_color);
                txt_configure.setTextColor(this.getResources().getColor(Blackground_color));
                break;
            case 1:
                txt_show.setBackgroundResource(Select_color);
                txt_show.setTextColor(this.getResources().getColor(Blackground_color));
                break;
        }
    }
    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        public String Tag = "Tab";
        final int PAGE_COUNT = 2;
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);Log.i(Tag, "flag1.2");
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            Log.i(Tag,"Tab:getitem"+position);
            Fragment frame = null;
            switch(position){
                case 0:
                    frame = jay_receive.newInstance(position + 1);
                    break;
                case 1:
                    frame = jay_control.newInstance(position + 1);
                    break;
            }
            return frame;
        }
    }

}
