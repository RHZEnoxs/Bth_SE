package bluetooth.bth_se.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import bluetooth.bth_se.MainActivity;
import bluetooth.bth_se.R;

/**
 * Created by Enoxs on 2016/9/4.
 */
public class control_adapter extends RecyclerView.Adapter<control_adapter.ViewHolder> implements
        View.OnClickListener {
    private List<String> mTitle;
    public boolean Edit_flag = false;
    public int position_flag = 0;
    private AlertDialog mdialog;
    private EditText edt_title,edt_ctrl;
    public control_adapter(List<String> title, AlertDialog dialog, EditText tit, EditText ctr){
        mTitle = title;
        mdialog = dialog;
        edt_title = tit;
        edt_ctrl = ctr;
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jay_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意這裡使用getTag方法獲取數據
            mOnItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mTitle.size();
    }//獲取數據數量
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_card;
        public Button btn_card;
        public ViewHolder(View v) {
            super(v);
            txt_card = (TextView) v.findViewById(R.id.txt_card);
            btn_card = (Button) v.findViewById(R.id.btn_card);
        }
    }
    //AnimationDrawable ani = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.anim);
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       /* holder.lay1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:   //按下
                        break;
                }
                return false;
            }
        });*/
        holder.txt_card.setText(mTitle.get(position));
        //holder.btn_card.setText("SEND");
        holder.btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edit_flag){
                    if(edt_title.getVisibility() == View.GONE){
                        edt_title.setVisibility(View.VISIBLE);
                    }
                    edt_title.setText("" + mTitle.get(position));
                    edt_ctrl.setText(MainActivity.mConfig.get_ctrl(position));
                    position_flag = position;
                    mdialog.show();
                }else{
                    if(MainActivity.mBth.Bth_flag){
                            MainActivity.mControl.Bth_SendData(MainActivity.mConfig.get_ctrl(position));
                    }
                }
            }
        });
        //mDevId
        holder.itemView.setTag(position);
    }
}
