package bluetooth.bth_se.fragment;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bluetooth.bth_se.Bluetooth;
import bluetooth.bth_se.MainActivity;
import bluetooth.bth_se.R;

public class Bth_adapter extends BaseAdapter{
    Context mContext;
    Bluetooth aBth;
    android.app.AlertDialog ada_dialog;
    ArrayList al_text;
//    Context context,Bluetooth bth,android.app.AlertDialog ad,ArrayList<String> al_show
    public Bth_adapter(Context context,Bluetooth bth,android.app.AlertDialog ad,ArrayList<String> al_show){
        mContext = context;
        aBth = bth;
        ada_dialog = ad;
        al_text = al_show;
    }

    @Override
    public int getCount() {
        return al_text.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if(v == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_list_block, null);
            holder = new Holder();
            holder.text = (TextView) v.findViewById(R.id.lst_text);
            holder.llay = (LinearLayout) v.findViewById(R.id.lay_select);
            v.setTag(holder);
        } else{
            holder = (Holder) v.getTag();
        }
        holder.text.setText(al_text.get(position) + "");
        holder.llay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try{
                    aBth.openBT(position);
                    ada_dialog.cancel();
                    aBth.Bth_flag = true;
                }catch(Exception e){
                }
                if(aBth.Bth_flag){
                    MainActivity.mReceive.txt_inf.setText("Bluetooth Connect\n");
                }
            }
        });
        return v;
    }
    class Holder{
        TextView text;
        LinearLayout llay;
    }
}
