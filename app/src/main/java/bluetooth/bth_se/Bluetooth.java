package bluetooth.bth_se;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import bluetooth.bth_se.fragment.Bth_adapter;

public class Bluetooth {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] rBuffer;
    public int pos;
    volatile boolean stopWorker;
    // --- add itme ---
    public boolean Bth_flag = false;
    public Bluetooth(){
    }
    public boolean findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean mBthAdp = false;
        if(mBluetoothAdapter == null)
        {
            mBthAdp = false;//"No Bluetooth Device";
        }
        if(!mBluetoothAdapter.isEnabled())
        {
            mBthAdp = false;// "Bluetooth Close"; and open it
        }
        else
        {
            mBthAdp = true ;// "Bluetooth Open";
        }
        return mBthAdp;
    }
    //--- add item ---
    ArrayList mArrayAdapter = new ArrayList();//List BlueTooth Device
    public void ListBT(ArrayList<String> mDevList,Bth_adapter mBthAdapter){//,Bth_adapter mBthAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            mArrayAdapter.clear();
            for(BluetoothDevice device : pairedDevices)
            {
                mArrayAdapter.add(device);
                mDevList.add(device.getName());
            }
        }
           mBthAdapter.notifyDataSetChanged();
    }

    public boolean Scan_flag = false;
    public ArrayList<String> bth_DevList;
    public Bth_adapter bth_Adapter;
    public void startScanBT(Context mContext ,ArrayList<String> mDevList ,Bth_adapter mBthAdapter){//
        bth_DevList = mDevList;
        bth_Adapter = mBthAdapter;
        Scan_flag = true;
        mArrayAdapter.clear();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }
    public void stopScanBT(){
        if(Scan_flag){
            Scan_flag = false;
            mBluetoothAdapter.cancelDiscovery();
        }
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            // 當收尋到裝置時
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                // 取得藍芽裝置這個物件
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mArrayAdapter.add(device);
                bth_DevList.add(device.getName());
                bth_Adapter.notifyDataSetChanged();
            }
        }
    };


    //--- add item ---
    public void openBT(int i) throws IOException
    {
        stopScanBT();
        mmDevice = (BluetoothDevice) mArrayAdapter.get(i);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        beginListenForData(); //開始傾聽藍芽裝置的資料
    }
    public byte show;String OutputStr;
    public void beginListenForData()
    {
        final Handler handler = new Handler();
        stopWorker = false;
        pos = 0;
        rBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() //建立一條新執行緒進入傾聽來自藍芽裝置資料輸入程序
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            final byte[] data = new byte[bytesAvailable];
                            mmInputStream.read(data);


                                handler.post(new Runnable()
                                {
                                    public void run()
                                    {
                                        for(int i=0;i<data.length;i++){
                                            show = data[i];
                                            OutputStr = String.format("0x%02x", show);
                                            MainActivity.mReceive.txt_inf.append(OutputStr + " ");
//                                            rBuffer[pos] = show;
//                                            pos++;
//                                            if(pos >= 1024){
//                                                pos = 0;
//                                            }
                                        }
                                    }
                                });



                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void sendData(byte [] Byte_arr) throws IOException
    {
        mmOutputStream.write(Byte_arr);
    }

    public void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        Bth_flag = false;
    }


}
