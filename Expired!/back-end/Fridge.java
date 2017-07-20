/*
 * Fridge.java
 *
 *  Created on: Mar 24, 2017
 *      Author: thele
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fridge
{
    Map<String, HashMap<String, Item>> purFridge = new HashMap<String, HashMap<String, Item>>();
    Map<String, HashMap<String, Item>> expFridge = new HashMap<String, HashMap<String, Item>>();
    Map<String, Pair> expdates = new HashMap<String, Pair>();

    void add(Item item)
    {
        String strDayExp = item.getDateExpired();
        String strDayPur = item.getDatePurchased();

        // if no day expired was given
        if (strDayExp.length() == 0)
        {
            // parse day exp + 
        }

        HashMap<String, Item> foundMap1 = expFridge.get(strDayExp);
        HashMap<String, Item> foundMap2 = purFridge.get(strDayPur);

        String itemName = item.getName();

        //TODO: int dayExp = calcExp(itemName);

        // add the element to the set if found
        if (foundMap1 != null)
        {
            foundMap1.put(itemName, item);
        }
        else // otherwise create a new map and insert the item
        {
            foundMap1 = new HashMap<String, Item>();
            foundMap1.put(itemName, item);
            expFridge.put(strDayExp, foundMap1);
        }
        // add the element to the set if found
        if (foundMap2 != null)
        {
            foundMap2.put(itemName, item);		
        }
        else // otherwise create a new map and insert the item
        {
            foundMap2 = new HashMap<String, Item>();
            foundMap2.put(itemName, item);
            purFridge.put(strDayPur, foundMap2);
        }
    }

    void remove(Item item)
    {
        String itemName = item.getName();
        String dateExp = item.getDateExpired();
        String datePur = item.getDatePurchased();

        HashMap<String, Item> foundMap1 = expFridge.get(dateExp);
        foundMap1.remove(itemName);

        HashMap<String, Item> foundMap2 = purFridge.get(datePur);
        foundMap2.remove(itemName);
    }

    void printDate(String date)
    {
        String day;
        String month;
        String year;

        day = date.substring(6, 8);
        month = date.substring(4, 6);
        year = date.substring(0, 4);

        System.out.print(month + "/" + day + "/" + year);
    }

    void printContents(int amount)
    {
        int counter = 0;
        // iterates through maps
        for (HashMap<String, Item> map : expFridge.values())
        {
            // iterates through items
            for (Item item : map.values())
            {
                if (counter >= amount) return; // stop when amount is reached
                counter++;

                System.out.print("Item: " + item.getName() + "; Date Bought: ");
                printDate(item.getDatePurchased());
                System.out.print("; Date Expires: ");
                printDate(item.getDateExpired());
                System.out.println("");
            }
        }
    }

    void printRecent(int amount)
    {
        int counter = 0;
        // iterates through maps
        for (HashMap<String, Item> map : purFridge.values())
        {
            // iterates through items
            for (Item item : map.values())
            {
                if (counter >= amount) return; // stop when amount is reached
                counter++;

                System.out.print("Item: " + item.getName() + "; Date Bought: ");
                printDate(item.getDatePurchased());
                System.out.print("; Date Expires: ");
                printDate(item.getDateExpired());
                System.out.println("");
            }
        }
    }

    String calcExp(String item_name, String type)
    {
        SimpleDateFormat dateFormat = new SimpleDataFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFormat.parse(item_name));
        int numofDays;
        if (type == "fridge")
        {

        }
        else if (type == "freeze")
        {

        }
        cal.add(Calendar.DATE, numofDays);
        // get today's date and add # of days stored in database field
        // search through database
        // set field of item to date found
        return dateExp;
    }

    // on program startup, load the expiration dates, so user has quick access to them
    void readExpired(String fileName)
    {
        Scanner file;
        File f = new File("expdate.txt");
        // open file
        try
        {
            file = new Scanner(f);
        }
        // if you can't, create a new one
        catch(Exception e)
        {
            System.out.println("Could not find file, creating a new one now.");
            try
            {
                f.createNewFile();
            }
            catch(Exception e2)
            {
                System.err.println("This shouldn't be caught.");
                e2.printStackTrace();
                throw new RuntimeException();
            }
            try
            {
                file = new Scanner(f);
            }
            catch(Exception e3)
            {
                System.err.println("This shouldn't be caught either.");
                e3.printStackTrace();
                throw new RuntimeException();
            }
        }
        // reads from file and fills up the expired hashtable
        while(file.hasNext())
        {
            String name = file.next();
            String fridgeDate = file.next();
            String freezerDate = file.next();
            expdates.put(name, new Pair(fridgeDate, freezerDate));
        }
    }

    // used when user wants to change an expiration date or add one of their own
    void writeExpired(String fileName)
    {

    }

    // on program startup, loads up the list
    void readList(String fileName)
    {

    }

    // when user adds an entry, we write it to the list file
    void writeList(String fileName)
    {

    }
}