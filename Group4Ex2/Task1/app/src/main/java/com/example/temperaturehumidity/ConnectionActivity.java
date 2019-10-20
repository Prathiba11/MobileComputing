package com.example.temperaturehumidity;

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

//import static com.example.temperaturehumidity.MainActivity.HUMIDITY_BTN_PRESSED;
//import static com.example.temperaturehumidity.MainActivity.TEMPERATURE_BTN_PRESSED;
import static com.example.temperaturehumidity.MainActivity.bluetoothAdapter;
//import static com.example.temperaturehumidity.MainActivity.TEMPERATURE_BTN_PRESSED;
//import static com.example.temperaturehumidity.MainActivity.HUMIDITY_BTN_PRESSED;

public class ConnectionActivity extends AppCompatActivity {

    public TextView InfoTextView;
    int HUMIDITY_BTN_PRESSED;
    int TEMPERATURE_BTN_PRESSED;
    public Button HumidityValue;
    public Button TemperatureValue;
    private boolean mScanning;
    private int x;
    private Handler handler = new Handler();
    /*public UUID[] uuidss = new UUID()
    uuidss[0] =*/
    String[] FanUUID = {"00000002-0000-0000-FDFD-FDFDFDFDFDFD"};
    private static final UUID WeatherServiceUUID = UUID.fromString("00000002-0000-0000-FDFD-FDFDFDFDFDFD");
    private static final UUID TemperatureCharacteristicUUID = UUID.fromString("00002a1c-0000-1000-8000-00805f9b34fb");
    private static final UUID HumidityCharacteristicUUID = UUID.fromString("00002a6f-0000-1000-8000-00805f9b34fb");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    String[] FanName = {"F6:B6:2A:79:7B:5D"};
    //public UUID FanServiceUUID = UUID.fromString("00000001-0000-0000-FDFD-FDFDFDFDFDFD");
    public BluetoothLeScanner bluetoothLeScanner;

    public ArrayList<BluetoothDevice> mBluetoothDeviceList = new ArrayList();
    public BluetoothDevice BTdevice;
    public String BTDeviceName;
    public String BTDeviceAddress;
    public static BluetoothGatt mGatt;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 100000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HUMIDITY_BTN_PRESSED=MainActivity.HUMIDITY_BTN_PRESSED;
        TEMPERATURE_BTN_PRESSED=MainActivity.TEMPERATURE_BTN_PRESSED;
        //Log.i("TEst passing var",Integer.toString(x));
        /*String TEMPERATURE_BTN_PRESSED=null;
        TEMPERATURE_BTN_PRESSED=savedInstanceState.getString(TEMPERATURE_BTN_PRESSED);
        String HUMIDITY_BTN_PRESSED=null;
        HUMIDITY_BTN_PRESSED=savedInstanceState.getString(HUMIDITY_BTN_PRESSED);*/

        setContentView(R.layout.activity_connection);
        InfoTextView = findViewById(R.id.InfoText);
        //HumidityValue = findViewById(R.id.Hum);
        //TemperatureValue = findViewById(R.id.Temp);
        //startScanning();
        //startScanning();
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
            //InfoTextView.setText("Device Address" + BTDeviceAddress);
            //SystemClock.sleep(7000);
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

        InfoTextView.setText("Found IPVS-Weather");
        mGatt = deviceToConnect.connectGatt(ConnectionActivity.this, false, gattCallback);

        /*if (mGatt == null) {
            mGatt = device.connectGatt(ScanDevices.this, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }*/
    }




    private void scanLeDevice(final boolean enable) {

        Log.i("Entered ScanLEDeive","Entered ScanLEDeive");
        Log.i("Test passing var",Integer.toString(x));
        if (enable) {
            InfoTextView.setText("Scanning");
            //SystemClock.sleep(7000);
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                public void run() {
                    mScanning = false;
                    //startScanning(FanUUID);
                    bluetoothLeScanner.stopScan(scanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            //startScanning(FanUUID);

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


    public void startScanning(String[] uuids) {
        Log.i("Entered Start Scanning","Entered Start Scanning");
        if (uuids == null || uuids.length == 0) {
            InfoTextView.setText("NO Device Found");
            return;
        }
        InfoTextView.setText("UUID "+ uuids);
        List<ScanFilter> filterList = createScanFilterList(uuids);
        ScanSettings scanSettings = createScanSettings();
        bluetoothLeScanner.startScan(filterList, scanSettings, scanCallback);
    }

    private List<ScanFilter> createScanFilterList(String[] uuids) {
        List<ScanFilter> filterList = new ArrayList<>();
        for (String uuid : uuids) {
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid.fromString(uuid))
                    .build();
            filterList.add(filter);
        };
        return filterList;
    }

    // Perform scan in Balanced Power Mode
    private ScanSettings createScanSettings() {
        ScanSettings settingsscan = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build();
        return settingsscan;
    }


    public void stopScanning() {
        bluetoothLeScanner.stopScan(scanCallback);
    }

/*
    public List<BluetoothDevice> getFoundDeviceList() {
        return mBluetoothDeviceList;
    }
*/

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
            /*List<BluetoothGattService> services = gatt.getServices();
            Log.i("Services", services.toString());
            for (BluetoothGattService eachService : services )
            {
                eachService.getUuid();
            }
            // Services will have all services of that BLE device
            InfoTextView.setText("Discovered Services:" + services.toString());
            Log.i("onServicesDiscovered", services.toString());
            SystemClock.sleep(100000);
            gatt.readCharacteristic(services.get(1).getCharacteristics().get
                    (0));*/
            BluetoothGattCharacteristic Tempcharacteristic;
            BluetoothGattCharacteristic Humiditycharacteristic;

            if (TEMPERATURE_BTN_PRESSED == 1){

                Tempcharacteristic =
                        gatt.getService(WeatherServiceUUID)
                                .getCharacteristic(TemperatureCharacteristicUUID);
                gatt.setCharacteristicNotification(Tempcharacteristic, true);
                BluetoothGattDescriptor descriptor =
                        Tempcharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);

                descriptor.setValue(
                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);

                gatt.readCharacteristic(Tempcharacteristic);
            }
            else if (HUMIDITY_BTN_PRESSED ==1){

                Humiditycharacteristic =
                        gatt.getService(WeatherServiceUUID)
                                .getCharacteristic(HumidityCharacteristicUUID);
                gatt.setCharacteristicNotification(Humiditycharacteristic, true);
                BluetoothGattDescriptor descriptor =
                        Humiditycharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);

                descriptor.setValue(
                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);

                gatt.readCharacteristic(Humiditycharacteristic);
            }




        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i("dinesh", "dinesh is here");

            Log.i("dinesh",Integer.toString(TEMPERATURE_BTN_PRESSED) );
            if(TEMPERATURE_BTN_PRESSED==1)
            {
                if(characteristic == null){
                    Log.i("Nullllll", "Nullllllllll");
                }
                //int temperature = characteristic.FORMAT_UINT32;
                Log.i("dineshTemp", "dinesh is in temp");
                int temperature = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,1);
                //int temperature = Tempcharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,1);

                float ftemperature = temperature/(float)100.00;
                Log.i("Temperature: " , Integer.toString(temperature));

                InfoTextView.setText("Temperature: " + ftemperature+ "deg Cel");

                Log.i("onCharacteristicRead", characteristic.toString());
                //gatt.disconnect();
            }
            else if(HUMIDITY_BTN_PRESSED==1){
                if(characteristic == null){
                    Log.i("Nullllll", "Nullllllllll");
                }
                //int temperature = characteristic.FORMAT_UINT32;
                int humidity = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,0);
                //int temperature = Tempcharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,1);

                float fhumidity = humidity/(float)100.00;
                Log.i("Humidity: " , Float.toString(humidity));

                InfoTextView.setText("humidity: " + fhumidity+ " %");

                Log.i("onCharacteristicRead", characteristic.toString());
                //gatt.disconnect();
            }

        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {

            if(TEMPERATURE_BTN_PRESSED==1)
            {
                if(characteristic == null){
                    Log.i("Nullllll", "Nullllllllll");
                }
                //int temperature = characteristic.FORMAT_UINT32;
                int temperature = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,1);
                //int temperature = Tempcharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,1);

                float ftemperature = temperature/(float)100.00;
                Log.i("Temperature: " , Integer.toString(temperature));

                InfoTextView.setText("Temperature: " + ftemperature+ "deg Cel");

                Log.i("onCharacteristicRead", characteristic.toString());
                //gatt.disconnect();
            }
            else if(HUMIDITY_BTN_PRESSED==1){
                if(characteristic == null){
                    Log.i("Nullllll", "Nullllllllll");
                }
                //int temperature = characteristic.FORMAT_UINT32;
                int humidity = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,0);
                //int temperature = Tempcharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16 ,1);

                float fhumidity = humidity/(float)100.00;
                Log.i("Humidity: " , Float.toString(fhumidity));

                InfoTextView.setText("humidity: " + fhumidity+ " %");

                Log.i("onCharacteristicRead", characteristic.toString());
                //gatt.disconnect();
            }
            }

            };



    @Override
    public void onBackPressed() {
        // your code.
        Toast.makeText(ConnectionActivity.this, "Back Button Pressed",
                Toast.LENGTH_LONG).show();
        try {
            mGatt.disconnect();
        }
        catch (Exception e){
            Log.i("mGatt","mGatt disconnect check");
        }
        //Intent goToMainActivity = new Intent(ConnectionActivity.this, MainActivity.class);
        HUMIDITY_BTN_PRESSED = 0;
        TEMPERATURE_BTN_PRESSED = 0;
        ConnectionActivity.this.finish();
        //ConnectionActivity.this.startActivity(goToMainActivity);

    }

    /*
    private LeDeviceListAdapter leDeviceListAdapter;
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            leDeviceListAdapter.addDevice(device);
                            leDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    val leDeviceListAdapter: LeDeviceListAdapter = ...

    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
            runOnUiThread {
        leDeviceListAdapter.addDevice(device)
        leDeviceListAdapter.notifyDataSetChanged()
    }
    } */
}
