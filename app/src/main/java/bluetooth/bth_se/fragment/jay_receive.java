package bluetooth.bth_se.fragment;


import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import bluetooth.bth_se.Bluetooth;
import bluetooth.bth_se.MainActivity;
import bluetooth.bth_se.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class jay_receive extends Fragment {

    public static final String SHOW_PAGE = "JAY_SHOW";

    private int mPage;

    public static jay_receive newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(SHOW_PAGE, page);
        jay_receive fragment = new jay_receive();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(SHOW_PAGE);
    }

    public jay_receive() {
        // Required empty public constructor
    }
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_receive, container, false);
        initView();
        return rootView;
    }

    private ImageButton fab_menu,fab_clear,fab_dislink;
    public TextView txt_inf;
    public boolean fab_flag = false,bth_flag=false;
    private void initView(){
        fab_menu = (ImageButton) rootView.findViewById(R.id.fab_menu);
        fab_clear = (ImageButton) rootView.findViewById(R.id.fab_clear);
        fab_dislink = (ImageButton) rootView.findViewById(R.id.fab_dislink);
        txt_inf = (TextView) rootView.findViewById(R.id.txt_inf);
        txt_inf.setText("Welecome to use Bluetooth Simple Example Tool.");
        List_Dialog();
        fab_menu.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(MainActivity.mBth.Bth_flag){
                    fab_status();
                }else{
                    Animation anim_menu = new RotateAnimation(360, 0 ,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    anim_menu.setDuration(500);
                    anim_menu.setStartOffset(50);
                    anim_menu.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            // TODO Auto-generated method stub
                        if(MainActivity.mBth.Bth_flag){
                            Bth_dialog.show();
                        }else{
                            if(MainActivity.mBth.findBT()){
                                dev_list.clear();
                                MainActivity.mBth.ListBT(dev_list,mAdapter);
                                Bth_dialog.show();
                            }
                            else{
                                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBluetooth,0);
                            }
                        }
                        }
                        public void onAnimationRepeat(Animation animation) {
                            // TODO Auto-generated method stub
                        }
                        public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
                        }
                    });
                    fab_menu.startAnimation(anim_menu);

                }
            }
        });

        fab_dislink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.mBth.Bth_flag){
                    try {
                        MainActivity.mBth.closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fab_status();
            }
        });
        fab_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_inf.setText("");
                MainActivity.mBth.pos = 0 ;
            }
        });


        // --- View Effect ---
        fab_menu.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    fab_menu.setBackgroundResource(R.drawable.circle_red);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    fab_menu.setBackgroundResource(R.drawable.circle_pupler);
                }
                return false;
            }
        });
        fab_clear.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    fab_clear.setBackgroundResource(R.drawable.circle_red);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    fab_clear.setBackgroundResource(R.drawable.circle_pupler);
                }
                return false;
            }
        });
        fab_dislink.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    fab_dislink.setBackgroundResource(R.drawable.circle_red);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    fab_dislink.setBackgroundResource(R.drawable.circle_pupler);
                }
                return false;
            }
        });
    }
    public Bth_adapter mAdapter;
    public ArrayList<String> dev_list = new ArrayList<>();
    public android.app.AlertDialog Bth_dialog;
    public LayoutInflater bthInflater;View bth_alertView;
    public LinearLayout lay_scanner;
    public void List_Dialog() {//自訂義布局
        Bth_dialog= new android.app.AlertDialog.Builder(getActivity()).create();
        bthInflater = LayoutInflater.from(getActivity());
        bth_alertView = bthInflater.inflate(R.layout.item_list, null);
        ListView lst = (ListView) bth_alertView.findViewById(R.id.lst);
        lay_scanner = (LinearLayout) bth_alertView.findViewById(R.id.lay_scanner);
        mAdapter = new Bth_adapter(getActivity(),MainActivity.mBth,Bth_dialog,dev_list);
        lst.setAdapter(mAdapter);
        lay_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mReceive.dev_list.clear();
                MainActivity.mBth.startScanBT(getContext(),dev_list,mAdapter);
            }
        });

        Bth_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
               if(MainActivity.mBth.Scan_flag){
                   MainActivity.mBth.stopScanBT();
                }
                Animation anim_menu = new RotateAnimation(0, 360 ,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim_menu.setDuration(500);
                anim_menu.setStartOffset(50);
                fab_menu.startAnimation(anim_menu);
            }
        });
        Bth_dialog.setView(bth_alertView);
        Bth_dialog.setCancelable(true);
    }
    private void fab_status(){
        if(fab_flag){
            fab_flag = false;
            Animation anim_menu = new RotateAnimation(0, 360 ,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim_menu.setDuration(500);
            anim_menu.setStartOffset(50);
            Animation anim_1 = new AlphaAnimation(1.0f, 0.0f);
            anim_1.setDuration(500);
            anim_1.setStartOffset(450);
            Animation anim_2 = new AlphaAnimation(1.0f, 0.0f);
            anim_2.setDuration(500);
            anim_2.setStartOffset(300);

//            anim_2.setRepeatCount(Animation.INFINITE);
            fab_menu.startAnimation(anim_menu);
            fab_clear.startAnimation(anim_1);
            fab_clear.setVisibility(View.GONE);
            fab_dislink.startAnimation(anim_2);
            fab_dislink.setVisibility(View.GONE);
        }
        else{
            Animation anim_menu = new RotateAnimation(360, 0 ,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim_menu.setDuration(500);
            anim_menu.setStartOffset(50);
            Animation anim_1 = new AlphaAnimation(0.0f, 1.0f);
            anim_1.setDuration(500);
            anim_1.setStartOffset(150);
            Animation anim_2 = new AlphaAnimation(0.0f, 1.0f);
            anim_2.setDuration(500);
            anim_2.setStartOffset(300);

//            anim_2.setRepeatCount(Animation.INFINITE);
            fab_menu.startAnimation(anim_menu);
            fab_flag = true;
            fab_clear.setVisibility(View.VISIBLE);
            fab_clear.startAnimation(anim_1);
            fab_dislink.setVisibility(View.VISIBLE);
            fab_dislink.startAnimation(anim_2);
        }

    }
    public void onStop(){
        super.onStop();
        MainActivity.mReceive = null;
    }
    public void onResume(){
        super.onResume();
        MainActivity.mReceive = this;
    }


}
