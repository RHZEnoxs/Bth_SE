package bluetooth.bth_se;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Configuration {
    private Context context;
    private SharedPreferences preferences;
    private Editor editor;
    public Configuration(Context context){
        this.context=context;
        preferences =context.getSharedPreferences("bth_se", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void setInit_flag(boolean init){// t
        editor.putBoolean("bth_init", init).commit();
    }
    public void set_Auto(boolean auto){
        editor.putBoolean("bth_auto",auto).commit();
    }
    public void set_freq(int freq){
        editor.putInt("bth_freq",freq).commit();
    }
    public void set_title(int position,String title){
        editor.putString("bth_title_" + position, title).commit();
    }

    public void set_ctrl(int position,String ctrl){
        editor.putString("bth_ctrl_" + position, ctrl).commit();
    }
    public boolean getInit_flag(){
        return preferences.getBoolean("bth_init", true);
    }
    public String get_title(int position){
        return preferences.getString("bth_title_" + position, "");
    }
    public String get_ctrl(int position){
        return preferences.getString("bth_ctrl_" + position, "");
    }
    public boolean get_Auto(){
        return preferences.getBoolean("bth_auto", false);
    }
    public int get_freq(){
        return preferences.getInt("bth_freq", 5000);
    }

}
