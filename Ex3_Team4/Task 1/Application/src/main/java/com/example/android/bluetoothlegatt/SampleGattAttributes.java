/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import java.util.HashMap;

public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String TEMPERATURE_MEASURE = "00002a1c-0000-1000-8000-00805f9b34fb";
    public static String HUMIDITY_MEASURE = "00002a6f-0000-1000-8000-00805f9b34fb";
    public static String INTENSITY_MEASURE = "10000001-0000-0000-fdfd-fdfdfdfdfdfd";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "0000feaa-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("00000002-0000-0000-fdfd-fdfdfdfdfdfd", "BLE Weather Service");
        attributes.put("00000001-0000-0000-fdfd-fdfdfdfdfdfd", "BLE Fan Control Service");
        // Sample Characteristics.

        attributes.put(TEMPERATURE_MEASURE, "Temperature Measurement");
        attributes.put(HUMIDITY_MEASURE, "Humidity Measurement");
        attributes.put(INTENSITY_MEASURE, "Intensity Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
