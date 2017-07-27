/*
 * Fridge.java
 *
 *  Created on: Mar 24, 2017
 *      Author: thele
 */

import java.io.FileWriter;
import java.util.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Fridge
{
    Map<String, TreeMap<String, Item>> expFridge = new TreeMap<String, TreeMap<String, Item>>();
    Map<String, TreeMap<String, Item>> purFridge = new TreeMap<String, TreeMap<String, Item>>();
    Map<String, PairofDates> expDates = new HashMap<String, PairofDates>();

    void add(Item item)
    {
        String strDayExp = item.getDateExpired();
        String strDayPur = item.getDatePurchased();
        String itemName = item.getName();
        String type = item.getStorageType();

        // if no day expired was given, calculate it using hashmap of expected expiration date
        if (strDayExp.length() == 0)
        {
            strDayExp = calcExp(itemName, type, strDayPur);
            item.setDateExpired(strDayExp);
        }

        TreeMap<String, Item> foundMap1 = expFridge.get(strDayExp);
        TreeMap<String, Item> foundMap2 = purFridge.get(strDayPur);

        // add the element to the set if found
        if (foundMap1 != null)
        {
            foundMap1.put(itemName, item);
        }
        else // otherwise create a new map and insert the item
        {
            foundMap1 = new TreeMap<String, Item>();
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
            foundMap2 = new TreeMap<String, Item>();
            foundMap2.put(itemName, item);
            purFridge.put(strDayPur, foundMap2);
        }
    }

    void remove(Item item)
    {
        String itemName = item.getName();
        String dateExp = item.getDateExpired();
        String datePur = item.getDatePurchased();

        TreeMap<String, Item> foundMap1 = expFridge.get(dateExp);
        foundMap1.remove(itemName);

        TreeMap<String, Item> foundMap2 = purFridge.get(datePur);
        foundMap2.remove(itemName);
    }

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
            PairofDates value = expDates.get(key);
            System.out.println("Food: " + key);
            System.out.println("Fridge Days: " + value.getFridge());
            System.out.println("Freezer Days: " + value.getFreezer());
        }
    }

    String calcExp(String item_name, String type, String dayPurchased)
    {
        int numofDays = 0;
        LocalDate parsedDate = LocalDate.parse(dayPurchased, DateTimeFormatter.BASIC_ISO_DATE);
        PairofDates pair = expDates.get(item_name);
        if (type == "fridge")
        {
            numofDays = pair.getFridge();
        }
        else if (type == "freeze")
        {
            numofDays = pair.getFreezer();
        }
        parsedDate = parsedDate.plusDays(numofDays);
        return parsedDate.format(DateTimeFormatter.BASIC_ISO_DATE);
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
            expDates.put(name, new PairofDates(fridgeDate, freezerDate));
        }
        file.close();
    }

    // used when user wants to change an expiration date or add one of their own
    void writeExpired(String fileName)
    {

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
            add(item);
        }
        // reads from file and fills up expFridge
        while(file.hasNext())
        {
            String name = file.next();
            String datePur = file.next();
            String dateExp = file.next();
            String typeStorage = file.next();
            Item item = new Item(name, datePur, dateExp, typeStorage);
            add(item);
        }
        file.close();
    }

    // when user adds an entry, we write it to the list file
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