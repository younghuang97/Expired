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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class Fridge
{
    Map<String, HashMap<String, Item>> purFridge = new HashMap<String, HashMap<String, Item>>();
    Map<String, HashMap<String, Item>> expFridge = new HashMap<String, HashMap<String, Item>>();
    Map<String, PairofDates> expdates = new HashMap<String, PairofDates>();

    void add(Item item, String type)
    {
        String strDayExp = item.getDateExpired();
        String strDayPur = item.getDatePurchased();
        String itemName = item.getName();

        // if no day expired was given, calculate it using hashmap of expected expiration date
        if (strDayExp.length() == 0)
        {
            strDayExp = calcExp(itemName, type, strDayPur); 
            item.setDateExpired(strDayExp);
        }

        HashMap<String, Item> foundMap1 = expFridge.get(strDayExp);
        HashMap<String, Item> foundMap2 = purFridge.get(strDayPur);

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
        if (date.length() != 8) return;

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

    void printDatabase()
    {
        /*for ( PairofDates pair : expdates.values())
        {
            System.out.println("Fridge Days: " + pair.getFridge());
            System.out.println("Freezer Days: " + pair.getFreezer());
        }*/
        Set<String> keys = expdates.keySet();
        for ( String key : keys)
        {
            PairofDates value = expdates.get(key);
            System.out.println("Food: " + key);
            System.out.println("Fridge Days: " + value.getFridge());
            System.out.println("Freezer Days: " + value.getFreezer());
        }
    }

    String calcExp(String item_name, String type, String dayPurchased)
    {
        int numofDays = 0;
        LocalDate parsedDate = LocalDate.parse(dayPurchased, DateTimeFormatter.BASIC_ISO_DATE);
        PairofDates pair = expdates.get(item_name);
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
            int fridgeDate = Integer.parseInt(file.next());
            int freezerDate = Integer.parseInt(file.next());
            expdates.put(name, new PairofDates(fridgeDate, freezerDate));
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
            String datePur = file.next();
            String dateExp = file.next();
            String typeStorage = file.next();
            Item item = new Item(name, datePur, dateExp);
            add(item, typeStorage);
        }
        file.close();
    }

    // when user adds an entry, we write it to the list file
    void writeList()
    {

    }

    // change storage type and its respective expiration date
    void changeType(String name)
    {

    }
}