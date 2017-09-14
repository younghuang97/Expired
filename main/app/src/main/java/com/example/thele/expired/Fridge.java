package com.example.thele.expired;
/*
 * Fridge.java
 *
 *  Created on: Mar 24, 2017
 *      Author: thele
 */

import android.content.Context;
import android.util.Log;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

public class Fridge
{
    private static final String TAG = "Fridge";
    private Map<String, TreeMap<String, Item>> expFridge = new TreeMap<>();
    private Map<String, TreeMap<String, Item>> purFridge = new TreeMap<>();
    private Map<String, PairOfDates> expDates = new HashMap<>();
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
            if (strDayExp == "FAIL")
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

    /*
    Adds a PairOfDates to expDates HashMap
    TODO: If an expdate already exists, then it automatically replaces it, ask user for confirmation later
     */
    void addExpDate(PairOfDates date, String name)
    {
        expDates.put(name, date);
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
        expDates.remove(name);
    }

    /*
    Converts date of form "YYYYMMDD" into a prettier format: "MM/DD/YYYY"
     */
    String printPrettyDate(String date)
    {
        if (date.length() != 8) return null;

        String day;
        String month;
        String year;

        day = date.substring(6, 8);
        month = date.substring(4, 6);
        year = date.substring(0, 4);

        String prettyDate = month + "/" + day + "/" + year;
        return prettyDate;
    }

    /*
    Converts date of form "MM/DD/YYYY" into format: "YYYYMMDD"
     */
    String printDate(String date)
    {
        if (date.length() != 10) return null;

        String day;
        String month;
        String year;

        day = date.substring(3, 5);
        month = date.substring(0, 2);
        year = date.substring(6, 10);

        String mdate = year + month + day;
        return mdate;
    }

    /*
    Prints an amount number of item dates starting with the item with the
    nearest expiration date
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

    String calcExp(String item_name, String type, String dayPurchased)
    {
        int numOfDays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dayPurchased));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        PairOfDates pair;
        try
        {
            pair = expDates.get(item_name.toLowerCase());
        }
        catch(NullPointerException e)
        {
            Log.e(TAG, "Database doesn't have expiration dates for " +
                    item_name + ". Please enter an expiration date manually.");
            return "FAIL";
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


    // on program startup, load the expiration dates, so user has quick access to them
    void readDatabase(Context myContext)
    {
        Scanner file;
        // TODO: path needs to be changed later
        File f = new File(myContext.getFilesDir() + "/database.txt");
        // open file
        if (f.exists()) {
            try {
                file = new Scanner(f);
                // reads from file and fills up the expired hashtable
                while (file.hasNext()) {
                    String name = file.next();
                    int fridgeDate = Integer.parseInt(file.next());
                    int freezerDate = Integer.parseInt(file.next());
                    expDates.put(name, new PairOfDates(fridgeDate, freezerDate));
                }
                file.close();
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
                if (!f.canWrite())
                {
                    Log.d(TAG, "Can't write.");
                }
                if (!f.canRead())
                {
                    Log.d(TAG, "Can't read.");
                }
                f.createNewFile();
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
        // TODO: path needs to be changed later
        File file = new File(myContext.getFilesDir() + "/database.txt");
        FileWriter fWriter;
        try
        {

            fWriter = new FileWriter(file, false);
            // iterates through maps of expFridge
            Set<String> keys = expDates.keySet();
            for ( String key : keys)
            {
                PairOfDates value = expDates.get(key);
                fWriter.write(key + '\t' + value.getFridge() + '\t' + value.getFreezer() + '\n');
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
        Scanner file;
        // TODO: path needs to be changed later
        File f = new File(myContext.getFilesDir() + "/fridge.txt");
        // open file
        try
        {
            file = new Scanner(f);
            // reads from file and fills up expFridge
            while(file.hasNext())
            {
                String name = file.next();
                String datePur = file.next();
                String dateExp = file.next();
                String typeStorage = file.next();
                Item item = new Item(name, datePur, dateExp, typeStorage);
                addItem(item);
            }
            file.close();
        }
        // if you can't, create a new one
        catch(Exception e)
        {
            System.out.println("readList() failed to find fridge.txt. Attempting to create the file now...");
            try
            {
                f.createNewFile();
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
    void writeList(Context myContext)
    {
        // TODO: path needs to be changed later
        File file = new File(myContext.getFilesDir() + "/fridge.txt");
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
            Log.d(TAG, "writeList() failed to find fridge.txt.");
            throw new RuntimeException();
        }
    }
}