package bluetooth.bth_se.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import bluetooth.bth_se.MainActivity;
import bluetooth.bth_se.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class jay_control extends Fragment {

    public static final String SHOW_PAGE = "JAY_SHOW";

    private int mPage;

    public static jay_control newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(SHOW_PAGE, page);
        jay_control fragment = new jay_control();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(SHOW_PAGE);
    }

    public jay_control() {
        // Required empty public constructor
    }

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_control, container, false);
        initView();
        initrecyView();
        return rootView;
    }
    boolean edt_flag;
    int edt_length;
    private Button btn_send;
    private EditText edt_input;
    private CheckBox cbk_auto,cbk_edit;
    private void initView(){
        edt_input = (EditText) rootView.findViewById(R.id.edt_input);
        cbk_auto = (CheckBox) rootView.findViewById(R.id.cbx_auto);
        cbk_edit = (CheckBox) rootView.findViewById(R.id.cbx_edit);
        cbk_auto.setChecked(MainActivity.mConfig.get_Auto());
        cbk_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbk_auto.isChecked()){
                    Auto_Flag = true;
                    txt_title.setVisibility(View.VISIBLE);
                    edt_title.setText(MainActivity.mConfig.get_freq()+"");
                    edt_title.setInputType(InputType.TYPE_CLASS_NUMBER);
                    edt_ctrl.setVisibility(View.GONE);
                    dialog.show();
                }
            }
        });
        cbk_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_title.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                if(cbk_edit.isChecked()){
                    myAdapter.Edit_flag = true;
                }else{
                    myAdapter.Edit_flag = false;
                }

            }
        });
        btn_send = (Button) rootView.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_flag = true;
                edt_length = edt_input.getText().toString().length();
                for(int i=2;i<edt_length ;i=i+3){
                    if(edt_input.getText().toString().charAt(i) !=  ' '){
                        edt_flag = false;
                        break;
                    }

                }
                if(edt_flag){
                    if(MainActivity.mBth.Bth_flag ){
                        Bth_SendData(edt_input.getText().toString());
                    }
                }else{
                    ToastStr("格式錯誤!");
                }

            }
        });
        Edit_Dialog();
    }

    public ArrayList<String> Setting_Title;
    control_adapter myAdapter;
    RecyclerView mList;
    private void initrecyView() {
        Setting_Title = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            //"0x"+Integer.toHexString(Integer.parseInt(mPrefer.getDev_Inf(i, 1)))
            Setting_Title.add(MainActivity.mConfig.get_title(i));
        }
        myAdapter = new control_adapter(Setting_Title,dialog,edt_title,edt_ctrl);
        mList = (RecyclerView) rootView.findViewById(R.id.recy_view);
        final GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 3);
        gridManager.setOrientation(GridLayoutManager.VERTICAL);
        mList.setLayoutManager(gridManager);
        mList.setAdapter(myAdapter);
    }
    public boolean Auto_Flag = false;
    public AlertDialog dialog;
    public TextView txt_title;
    public EditText edt_title,edt_ctrl;
    public void Edit_Dialog() {//自訂義布局
        LayoutInflater adbInflater = LayoutInflater.from(getContext());
        dialog = new AlertDialog.Builder(getContext()).create();
        View alertView = adbInflater.inflate(R.layout.item_ctrl_cmd, null);
        txt_title = (TextView) alertView.findViewById(R.id.txt_title);
        edt_title = (EditText) alertView.findViewById(R.id.edt_title);
        edt_ctrl = (EditText) alertView.findViewById(R.id.edt_ctrl);
        Button btn_y = (Button) alertView.findViewById(R.id.btn1);
        btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Auto_Flag){
                    MainActivity.mConfig.set_freq(Integer.parseInt(edt_title.getText()+""));
                    dialog.cancel();
                }
                if(myAdapter.Edit_flag){
                    edt_flag = true;
                    edt_length = edt_ctrl.getText().toString().length();
                    for(int i=2;i<edt_length ;i=i+3){
                        if(edt_ctrl.getText().toString().charAt(i) !=  ' '){
                            edt_flag = false;
                            break;
                        }

                    }
                    if(edt_flag){
                        MainActivity.mConfig.set_title(myAdapter.position_flag,edt_title.getText()+"");
                        MainActivity.mConfig.set_ctrl(myAdapter.position_flag,edt_ctrl.getText()+"");
                        //edt_input.setText(myAdapter.position_flag + ":" + edt_title.getText());
                        Setting_Title.set(myAdapter.position_flag,MainActivity.mConfig.get_title(myAdapter.position_flag));
                        myAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                    else{
                        ToastStr("格式錯誤!");
                    }
                }
            }
        });
        Button btn_n = (Button) alertView.findViewById(R.id.btn2);
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.setView(alertView);
        dialog.setCancelable(true);
//        dialog.show();
    }

    public boolean auto_flag = false;
    public void atuo_data(){
        final Handler handler = new Handler();
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(auto_flag && MainActivity.mBth.Bth_flag){
                    handler.post(new Runnable(){
                        public void run(){
                            Bth_SendData("12 AA 15");
                            try{
                                Thread.sleep(MainActivity.mConfig.get_freq());
                            }catch (Exception e){
                            }
                        }});
                }
            }}).start();
    }
    public void  Bth_SendData(String msg){
        try {
            String results[] = msg.split(" ");
            byte[] data = new byte[results.length];
            for (int i = 0; i < data.length; i++){
                data[i] = (byte) Integer.parseInt(results[i], 16);
            }
            MainActivity.mBth.sendData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void ToastStr(String str){
        Toast.makeText(getActivity(), str,
                Toast.LENGTH_SHORT).show();
    }

    public void onStop(){
        super.onStop();
        MainActivity.mControl = null;
    }
    public void onResume(){
        super.onResume();
        MainActivity.mControl = this;
    }
}
