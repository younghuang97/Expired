/*
 * Fridge.java
 *
 *  Created on: Mar 24, 2017
 *      Author: thele
 */

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

public class Fridge
{
    Map<String, TreeMap<String, Item>> expFridge = new TreeMap<>();
    Map<String, TreeMap<String, Item>> purFridge = new TreeMap<>();
    Map<String, PairOfDates> expDates = new HashMap<>();

    /*
    Adds an item to both TreeMaps
     */
    void addItem(Item item)
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
    }

    /*
    Adds a PairOfDates to expDates HashMap
     */
    void addExpDate(PairOfDates date, String name)
    {
        if (expDates.containsKey(name))
        {
            expDates.replace(name, date);
        }
        else
        {
            expDates.put(name, date);
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
        expDates.remove(name);
    }

    /*
    Prints date of form "YYYYMMDD" in a prettier U.S format: "MM/DD/YYYY"
     */
    void printDate(String date)
    {
        if (date.length() != 8) return;

        String day;
        String month;
        String year;

        day = date.substring(6, 8);
        month = date.substring(4, 6);
        year = date.substring(0, 4);

        System.out.print(month + "/" + day + "/" + year);
    }

    /*
    Prints an amount number of item dates starting with the item with the
    nearest expiration date
     */
    void printDateExpired(int amount)
    {
        int counter = 0;
        // iterates through maps
        for (TreeMap<String, Item> map : expFridge.values())
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
                System.out.println("; Storage Type: " + item.getStorageType());
            }
        }
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

                System.out.print("Item: " + item.getName() + "; Date Bought: ");
                printDate(item.getDatePurchased());
                System.out.print("; Date Expires: ");
                printDate(item.getDateExpired());
                System.out.println("; Storage Type: " + item.getStorageType());
            }
        }
    }

    void printDatabase()
    {
        Set<String> keys = expDates.keySet();
        for ( String key : keys)
        {
            PairOfDates value = expDates.get(key);
            System.out.println("Food: " + key);
            System.out.println("Fridge Days: " + value.getFridge());
            System.out.println("Freezer Days: " + value.getFreezer());
        }
    }

    String calcExp(String item_name, String type, String dayPurchased)
    {
        int numOfDays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dayPurchased));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        PairOfDates pair;
        try
        {
            pair = expDates.get(item_name);
        }
        catch(NullPointerException e)
        {
            System.err.println("Database doesn't have expiration dates for " +
                    item_name + ". Please enter an expiration date manually.");
            return null;
        }
        if (type == "fridge")
        {
            numOfDays = pair.getFridge();
        }
        else if (type == "freeze")
        {
            numOfDays = pair.getFreezer();
        }
        c.add(Calendar.DATE, numOfDays);
        return sdf.format(c.getTime());
    }


    // on program startup, load the expiration dates, so user has quick access to them
    void readDatabase()
    {
        Scanner file;
        // TODO: path needs to be changed later
        File f = new File("C:\\Users\\thele\\IdeaProjects\\Expired!\\src\\database.txt");
        // open file
        try
        {
            file = new Scanner(f);
        }
        // if you can't, create a new one
        catch(Exception e)
        {
            System.out.println("readDatabase() failed to find file. Creating a new one now...");
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
            int fridgeDate = Integer.parseInt(file.next());
            int freezerDate = Integer.parseInt(file.next());
            expDates.put(name, new PairOfDates(fridgeDate, freezerDate));
        }
        file.close();
    }

    /* called anytime a change is made to the database of expiration dates to solidify changes
       TODO: Right now, I am fully re-writing the file after every change, later, if the user is only
       Adding something then the change can simply be appended
    */
    void writeDatabase()
    {
        // TODO: path needs to be changed later
        File file = new File("C:\\Users\\thele\\IdeaProjects\\Expired!\\src\\database.txt");
        FileWriter fWriter;
        try
        {
            fWriter = new FileWriter(file, false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("writeDatabase() failed to find file. Creating a new one now...");
            try
            {
                file.createNewFile();
            }
            catch(Exception e2)
            {
                System.err.println("This shouldn't be caught.");
                e2.printStackTrace();
                throw new RuntimeException();
            }
            try
            {
                fWriter = new FileWriter(file, false);
            }
            catch(Exception e3)
            {
                System.err.println("This shouldn't be caught either.");
                e3.printStackTrace();
                throw new RuntimeException();
            }
        }
        // iterates through maps of expFridge
        Set<String> keys = expDates.keySet();
        for ( String key : keys)
        {
            PairOfDates value = expDates.get(key);
            try
            {
                fWriter.write(key + '\t' + value.getFridge() + '\t' + value.getFreezer() + '\n');
            }
            catch(Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        try
        {
            fWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // on program startup, loads up the list
    void readList()
    {
        Scanner file;
        // TODO: path needs to be changed later
        File f = new File("C:\\Users\\thele\\IdeaProjects\\Expired!\\src\\fridge.txt");
        // open file
        try
        {
            file = new Scanner(f);
        }
        // if you can't, create a new one
        catch(Exception e)
        {
            System.out.println("readList() failed to find file. Creating a new one now...");
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
            if (name == "Switch") break;
            String datePur = file.next();
            String dateExp = file.next();
            String typeStorage = file.next();
            Item item = new Item(name, datePur, dateExp, typeStorage);
            addItem(item);
        }
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

    // called anytime a change is made to the database of items to solidify changes
    void writeList()
    {
        // TODO: path needs to be changed later
        File file = new File("C:\\Users\\thele\\IdeaProjects\\Expired!\\src\\fridge.txt");
        FileWriter fWriter;
        try
        {
            fWriter = new FileWriter(file, false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("writeList() failed to find file. Creating a new one now...");
            try
            {
                file.createNewFile();
            }
            catch(Exception e2)
            {
                System.err.println("This shouldn't be caught.");
                e2.printStackTrace();
                throw new RuntimeException();
            }
            try
            {
                fWriter = new FileWriter(file, false);
            }
            catch(Exception e3)
            {
                System.err.println("This shouldn't be caught either.");
                e3.printStackTrace();
                throw new RuntimeException();
            }
        }
        // iterates through maps of expFridge
        for (TreeMap<String, Item> map : expFridge.values())
        {
            // iterates through items
            for (Item item : map.values())
            {
                try {
                    fWriter.write(item.getName() + '\t' + item.getDatePurchased() + '\t' +
                            item.getDateExpired() + '\t' + item.getStorageType() + '\n');
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        }
        try
        {
            fWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}