package bluetooth.bth_se;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bluetooth.bth_se.fragment.jay_control;
import bluetooth.bth_se.fragment.jay_main;
import bluetooth.bth_se.fragment.jay_receive;

public class MainActivity extends AppCompatActivity implements Controller{
    public static jay_control mControl;
    public static jay_receive mReceive;

    public static Bluetooth mBth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_Config();
        if (savedInstanceState == null) {
            jay_main fragment = new jay_main();//first fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();
        }
        mBth = new Bluetooth();
    }
    public static Configuration mConfig;

    public void init_Config(){
        mConfig = new Configuration(getApplicationContext());
//        mConfig.setInit_flag(true);
        if(mConfig.getInit_flag()){
            mConfig.setInit_flag(false);
            for(int i=0;i<15;i++){
                mConfig.set_title(i,String.format("%03x", i+1));
                mConfig.set_ctrl(i,"AA FF " +String.format("%02x", i+1));
            }
        }
    }


}
