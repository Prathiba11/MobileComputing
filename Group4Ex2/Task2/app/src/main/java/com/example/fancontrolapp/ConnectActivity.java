package com.example.fancontrolapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.fancontrolapp.MainActivity.bluetoothAdapter;

public class ConnectActivity extends AppCompatActivity {

    public TextView InfoTextView;
    private boolean mScanning;
    private static int FanSpeed;
    private Handler handler = new Handler();

    private static final UUID FanServiceUUID = UUID.fromString("00000001-0000-0000-FDFD-FDFDFDFDFDFD");
    private static final UUID FanCharacteristicUUID = UUID.fromString("10000001-0000-0000-FDFD-FDFDFDFDFDFD");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002904-0000-1000-8000-00805f9b34fb");

    String[] FanName = {"F8:20:74:F7:2B:82"};
    public BluetoothLeScanner bluetoothLeScanner;

    public ArrayList<BluetoothDevice> mBluetoothDeviceList = new ArrayList();
    public BluetoothDevice BTdevice;
    public String BTDeviceName;
    public String BTDeviceAddress;
    public BluetoothGatt mGatt;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 1000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        FanSpeed = MainActivity.intensity_value;
        InfoTextView = findViewById(R.id.InfoText);
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        Log.i("Entered 2nd Activity", "ENtered 2nd Activity");
        scanLeDevice(true);
    }

    // Device scan callback.
    public ScanCallback scanCallback =  new ScanCallback()
    {
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("Inside ScanCallBack", "Inside ScanCallBack");
            super.onScanResult(callbackType, result);
            if (result == null) {
                return;
            }
            BTDeviceAddress = result.getDevice().getAddress();
            BTdevice = result.getDevice();
            Log.i("BTDEvice",BTdevice.toString());
            Log.i("BTDEviceAddress",BTDeviceAddress.toString());
            InfoTextView.setText("Device Address" + BTDeviceAddress);
            //SystemClock.sleep(7000);

            if (BTdevice == null || BTDeviceAddress == null) {
                return;
            }
            mBluetoothDeviceList.add(BTdevice);

            for (BluetoothDevice eachDevice : mBluetoothDeviceList) {
                if (eachDevice.getAddress().equals(FanName[0]) ) {
                    InfoTextView.setText("Found IPVS-Light");
                    scanLeDevice(false);// will stop after first device detection
                    connectToDevice(eachDevice);
                }
                //connectToDevice(BTdevice);
            }
        }
    };

    public void connectToDevice(BluetoothDevice deviceToConnect) {
        Log.i("Inside Connect2Device","Inside Connect2Device");
        if (deviceToConnect == null){
            Log.i("It is EMpty", "Empty");
            //SystemClock.sleep(10000);
        }
        InfoTextView.setText("Before Connection " + deviceToConnect.toString() );

        InfoTextView.setText("Found IPVS-Light");
        mGatt = deviceToConnect.connectGatt(ConnectActivity.this, false, gattCallback);

        /*if (mGatt == null) {
            mGatt = device.connectGatt(ScanDevices.this, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }*/
    }




    private void scanLeDevice(final boolean enable) {

        Log.i("Entered ScanLEDeive","Entered ScanLEDeive");
        if (enable) {
            InfoTextView.setText("Scanning");
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(scanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;

            if (bluetoothLeScanner == null){
                Log.i("Scan","NULL");
            }
            bluetoothLeScanner.startScan(scanCallback);
        } else {
            mScanning = false;
            //stopScanning();
            bluetoothLeScanner.stopScan(scanCallback);
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("ENtered Gattcallback","ENtered Gattcallback");
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    InfoTextView.setText("STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    InfoTextView.setText("STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
                    InfoTextView.setText("STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            BluetoothGattCharacteristic FanCharacteristic =
                    gatt.getService(FanServiceUUID)
                            .getCharacteristic(FanCharacteristicUUID);

            Log.i("Intensity: ", Integer.toString(FanSpeed));
            // Offset value has to be found and Format has to be decided
            FanCharacteristic.setValue(FanSpeed, BluetoothGattCharacteristic.FORMAT_UINT16, 0);
            gatt.writeCharacteristic(FanCharacteristic);


           /* BluetoothGattDescriptor descriptor =
                    Tempcharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);

            descriptor.setValue(
                    BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);*/


        }

       /* @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

            Log.i("updatecalled","called");

            if(characteristic == null){
                Log.i("Nullllll", "Nullllllllll");
            }

        }*/


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic
                                                  characteristic, int status) {

            if(characteristic == null){
                Log.i("Nullllll", "Nullllllllll");
            }
            Log.i("Inside characteri","Inside characteristic write");
            int Fanspeed = ((FanSpeed / 65535) * 100);
            InfoTextView.setText("Fan is Running at " + Fanspeed + " % speed !!");
            //characteristic.setValue(FanSpeed, BluetoothGattCharacteristic.FORMAT_UINT16, 0);

        }

      /*  @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status){
            BluetoothGattCharacteristic FanCharacteristic =
                    gatt.getService(FanServiceUUID)
                            .getCharacteristic(FanCharacteristicUUID);
           // FanCharacteristic.setValue(new byte[]{1, 1});
           // gatt.writeCharacteristic(FanCharacteristic);
        }*/
    };



    @Override
    public void onBackPressed() {
        // your code.
        Toast.makeText(ConnectActivity.this, "Back Button Pressed",
                Toast.LENGTH_LONG).show();
        try {
            mGatt.disconnect();
        }
        catch (Exception e){
            Log.i("mGatt","mGatt disconnect check");
        }
        ConnectActivity.this.finish();
        //ConnectionActivity.this.startActivity(goToMainActivity);

    }

}
