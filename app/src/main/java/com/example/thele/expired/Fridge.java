package com.example.thele.expired;
/*
 * Fridge.java
 *
 *  Created on: Mar 24, 2017
 *      Author: thele
 */

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

public class Fridge
{
    private static final String TAG = "Fridge";
    private Map<String, TreeMap<String, Item>> expFridge = new TreeMap<>();
    private Map<String, TreeMap<String, Item>> purFridge = new TreeMap<>();
    private Map<String, PairOfDates> expDates = new TreeMap<>();
    private static Fridge fridge = null;

    private Fridge() { }

    public static Fridge getFridge(){
        if (fridge == null)
        {
            fridge = new Fridge();
        }
        return fridge;
    }

    /*
    Adds an item to both TreeMaps
     */
    boolean addItem(Item item)
    {
        String strDayExp = item.getDateExpired();
        String strDayPur = item.getDatePurchased();
        String itemName = item.getName();
        String type = item.getStorageType();

        // if no day expired was given, calculate it using hashmap of expected
        // expiration date
        if (strDayExp.length() == 0)
        {
            strDayExp = calcExp(itemName, type, strDayPur);
            if (strDayExp == null)
            {
                return false;
            }
            item.setDateExpired(strDayExp);
        }

        TreeMap<String, Item> foundMap1 = expFridge.get(strDayExp);
        TreeMap<String, Item> foundMap2 = purFridge.get(strDayPur);

        // addItem the element to the set if found
        if (foundMap1 != null)
        {
            foundMap1.put(itemName, item);
        }
        else // otherwise create a new map and insert the item
        {
            foundMap1 = new TreeMap<>();
            foundMap1.put(itemName, item);
            expFridge.put(strDayExp, foundMap1);
        }
        // addItem the element to the set if found
        if (foundMap2 != null)
        {
            foundMap2.put(itemName, item);		
        }
        else // otherwise create a new map and insert the item
        {
            foundMap2 = new TreeMap<>();
            foundMap2.put(itemName, item);
            purFridge.put(strDayPur, foundMap2);
        }
        return true;
    }

    String calcExp(String item_name, String type, String dayPurchased)
    {
        int numOfDays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dayPurchased));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        PairOfDates pair = expDates.get(item_name);

        if (pair == null) {
            return null;
        }

        if (type.equals("Fridge"))
        {
            numOfDays = pair.getFridge();
        }
        else if (type.equals("Freezer"))
        {
            numOfDays = pair.getFreezer();
        }
        cal.add(Calendar.DATE, numOfDays);
        return sdf.format(cal.getTime());
    }

    /*
    Adds a PairOfDates to expDates HashMap
    TODO: If an expdate already exists, then it automatically replaces it, ask user for confirmation later
     */
    void addExpDate(String name, PairOfDates pair) { expDates.put(name, pair); }

    /*
    Given a path to the JSON file, parses the data and writes it to the hashmaps
    TODO: Some duplicate names, need to get the subnames also
     */
    void addExpDatesFromJSON(Context myContext)  {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = myContext.getAssets().open("data/foodstorage.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str;
            while ((str=in.readLine()) != null) {
                buf.append(str);
            }
            in.close();

            JSONObject jObject = new JSONObject(buf.toString());
            Iterator<String> keys = jObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject a = jObject.getJSONObject(key);
                if (a != null) {
                    int fridge = 0;
                    int freeze = 0;
                    try {
                        fridge = a.getInt("fridge");
                    }
                    catch (JSONException e) {

                    }
                    try {
                        freeze = a.getInt("freeze");
                    }
                    catch (JSONException e) {

                    }
                    PairOfDates dates = new PairOfDates(key, fridge, freeze);
                    addExpDate(key, dates);
                }
            }
        }
        catch (IOException e) {
            Log.d(TAG, "Failed to open foodstorage.json");
        }
        catch (JSONException e) {
            Log.d(TAG, "Failed to parse foodstorage.json");
        }
    }

    /*
    Removes item from both TreeMaps
     */
    void removeItem(Item item)
    {
        String itemName = item.getName();
        String dateExp = item.getDateExpired();
        String datePur = item.getDatePurchased();

        TreeMap<String, Item> foundMap1 = expFridge.get(dateExp);
        foundMap1.remove(itemName);

        TreeMap<String, Item> foundMap2 = purFridge.get(datePur);
        foundMap2.remove(itemName);
    }

    /*
    Removes a PairOfDates from expDates HashMap
     */
    void removeExpDate(String name)
    {
        if (expDates.remove(name) == null) Log.d(TAG, "DAFAQ");
    }

    /*
    Converts date of form "YYMMDD" into a prettier format: "MM/DD/YY"
     */
    String printPrettyDate(String date)
    {
        if (date.length() != 6) return null;

        String day;
        String month;
        String year;

        day = date.substring(4, 6);
        month = date.substring(2, 4);
        year = date.substring(0, 2);

        String prettyDate = month + "/" + day + "/" + year;
        return prettyDate;
    }

    /*
    Converts date of form "MM/DD/YY" into format: "YYMMDD"
     */
    String printDate(String date)
    {
        if (date.length() != 8) return null;

        String day = date.substring(3, 5);
        String month = date.substring(0, 2);
        String year = date.substring(6, 8);

        return year + month + day;
    }

    /*
    Returns all stored items in order of earliest nearest expiration date
     */
    List<Item> returnDateExpired()
    {
        List<Item> list = new ArrayList<>();

        // iterates through maps
        for (TreeMap<String, Item> map : expFridge.values())
        {
            // iterates through items
            for (Item item : map.values())
            {
                list.add(item);
            }
        }
        return list;
    }

    /*
    Returns all stored items in order of earliest nearest expiration date
     */
    List<PairOfDates> returnDatabase()
    {
        List<PairOfDates> list = new ArrayList<>();
        for (PairOfDates date : expDates.values())
        {
            list.add(date);
        }
        return list;
    }

    /*
    Prints an amount number of item dates starting with the item with the
    nearest purchase date
     */
    void printDatePurchased(int amount)
    {
        int counter = 0;
        // iterates through maps
        for (TreeMap<String, Item> map : purFridge.values())
        {
            // iterates through items
            for (Item item : map.values())
            {
                if (counter >= amount) return; // stop when amount is reached
                counter++;

                Log.d(TAG, "Item: " + item.getName() + "; Date Bought: " +
                        printPrettyDate(item.getDatePurchased()) + "; Date Expires: " +
                        printPrettyDate(item.getDateExpired()) + "; Storage Type: " +
                        item.getStorageType());
            }
        }
    }

    void printDatabase()
    {
        Set<String> keys = expDates.keySet();
        for ( String key : keys)
        {
            PairOfDates value = expDates.get(key);
            Log.d(TAG, "Food: " + key);
            Log.d(TAG, "Fridge Days: " + value.getFridge());
            Log.d(TAG, "Freezer Days: " + value.getFreezer());
        }
    }

    // on program startup, load the expiration dates, so user has quick access to them
    void readDatabase(Context myContext)
    {
        Scanner scanner;
        File internalStorageDir = myContext.getFilesDir();
        File file = new File(internalStorageDir, "database.txt");
        // open file
        if (file.exists()) {
            try {
                scanner = new Scanner(file);
                // reads from file and fills up the expired hashmap
                while (scanner.hasNext()) {
                    String name = scanner.nextLine().trim();
                    int fridgeDate = scanner.nextInt();
                    int freezerDate = scanner.nextInt();
                    expDates.put(name, new PairOfDates(name, fridgeDate, freezerDate));
                }
                scanner.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Log.d(TAG, "readDatabase() failed to find database.txt. Creating a new one now...");
            try {
                file.createNewFile();
                addExpDatesFromJSON(myContext);
                writeDatabase(myContext);
            } catch (Exception e2) {
                Log.e(TAG, "Failed to create database.txt.");
                e2.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    /* called anytime a change is made to the database of expiration dates to solidify changes
       TODO: Right now, I am fully re-writing the file after every change, later, if the user is only
       Adding something then the change can simply be appended
    */
    void writeDatabase(Context myContext)
    {
        File internalStorageDir = myContext.getFilesDir();
        File file = new File(internalStorageDir, "database.txt");
        FileWriter fWriter;
        try
        {
            fWriter = new FileWriter(file, false);
            // iterates through maps of expFridge
            Set<String> keys = expDates.keySet();
            for ( String key : keys)
            {
                PairOfDates value = expDates.get(key);
                fWriter.write(key + '\n' + value.getFridge() + ' ' + value.getFreezer() + ' ');
            }
            fWriter.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "writeDatabase() failed to find database.txt.");
            throw new RuntimeException();
        }
    }

    // on program startup, loads up the list
    void readList(Context myContext)
    {
        Scanner scanner;
        File internalStorageDir = myContext.getFilesDir();
        File file = new File(internalStorageDir, "fridge.txt");
        // open file
        try
        {
            scanner = new Scanner(file);
            // reads from file and fills up expFridge
            while(scanner.hasNext())
            {
                String name = scanner.next("\t");
                String datePur = scanner.next("\t");
                String dateExp = scanner.next("\t");
                String typeStorage = scanner.next();
                Item item = new Item(name, datePur, dateExp, typeStorage);
                addItem(item);
            }
            scanner.close();
        }
        // if you can't, create a new one
        catch(Exception e)
        {
            System.out.println("readList() failed to find fridge.txt. Attempting to create the file now...");
            try
            {
                file.createNewFile();
            }
            catch(Exception e2)
            {
                System.err.println("Failed to create fridge.txt.");
                e2.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    // called anytime a change is made to the database of items to solidify changes
    void updateFridge(Context myContext)
    {
        File internalStorageDir = myContext.getFilesDir();
        File file = new File(internalStorageDir, "fridge.txt");
        FileWriter fWriter;
        try
        {
            fWriter = new FileWriter(file, false);
            // iterates through maps of expFridge
            for (TreeMap<String, Item> map : expFridge.values())
            {
                // iterates through items
                for (Item item : map.values())
                {
                    fWriter.write(item.getName() + '\t' + item.getDatePurchased() + '\t' +
                            item.getDateExpired() + '\t' + item.getStorageType() + '\n');
                }
            }
            fWriter.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "updateFridge() failed to find fridge.txt.");
            throw new RuntimeException();
        }
    }
}