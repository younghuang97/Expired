/*
 * Fridge.cpp
 *
 *  Created on: Mar 24, 2017
 *      Author: thele
 */

#include "Fridge.h"
#include <iostream>

using namespace std;

void Fridge::add(Item* item)
{
	string strDayExp = item->getDateExpired();
	string strDayPur = item->getDatePurchased();

	auto foundMap1 = expFridge.find(strDayExp);
	auto foundMap2 = purFridge.find(strDayPur);

	string itemName = item->getName();

	//TODO: int dayExp = calcExp(itemName);

	// add the element to the set if found
	if (foundMap1 != expFridge.end())
	{
		foundMap1->second.insert(make_pair(itemName, item));
	}
	else // otherwise create a new map and insert the item
	{
		map<string, Item*> itemMap1;
		itemMap1.insert(make_pair(itemName, item));
		expFridge.insert(make_pair(strDayExp, itemMap1));
	}
	// add the element to the set if found
	if (foundMap2 != purFridge.end())
	{
		foundMap2->second.insert(make_pair(itemName, item));		
	}
	else // otherwise create a new map and insert the item
	{
		map<string, Item*> itemMap2;
		itemMap2.insert(make_pair(itemName, item));
		purFridge.insert(make_pair(strDayPur, itemMap2));
	}
}

void Fridge::remove(Item* item)
{
	string itemName = item->getName();
	string dateExp = item->getDateExpired();
	string datePur = item->getDatePurchased();

	auto foundMap1 = expFridge.find(dateExp);
	auto itemMap1 = foundMap1->second;
	itemMap1.erase(itemName);

	auto foundMap2 = purFridge.find(datePur);
	auto itemMap2 = foundMap2->second;
	itemMap2.erase(itemName);
}

void Fridge::printDate(string date)
{
	string day;
	string month;
	string year;

	day = date.substr(6, 2);
	month = date.substr(4, 2);
	year = date.substr(0, 4);

	cout << month << "/" << day << "/" << year;
}

void Fridge::printContents(int amount)
{
	int counter = 0;
	// iterates through sets
	for(auto it = expFridge.begin(); it != expFridge.end(); ++it)
	{
		auto itemMap = it->second;
		// iterates through items in each set
		for (auto it2 = itemMap.begin(); it2 != itemMap.end(); ++it2)
		{
			if (counter >= amount) return; // stop when amount is reached
			counter++;

			Item* item = it2->second;
			cout << "Item: " << item->getName() << "; Date Bought: ";
			printDate(item->getDatePurchased());
			cout << "; Date Expires: ";
			printDate(item->getDateExpired());
			cout << endl;
		}
	}
}

void Fridge::printRecent(int amount)
{
	int counter = 0;
	// iterates through items
	for(auto it = purFridge.begin(); it != purFridge.end(); ++it)
	{
		auto itemMap = it->second;
		// iterates through items in each set
		for (auto it2 = itemMap.begin(); it2 != itemMap.end(); ++it2)
		{
			if (counter >= amount) return; // stop when amount is reached
			counter++;

			Item* item = it2->second;
			cout << "Item: " << item->getName() << "; Date Bought: ";
			printDate(item->getDatePurchased());
			cout << "; Date Expires: ";
			printDate(item->getDateExpired());
			cout << endl;
		}
	}
}

//TODO:
int Fridge::calcExp(string item_name)
{
	int dateExp = 0;
	// get today's date and add # of days stored in database field
	// search through database
	// set field of item to date found
	return dateExp;
}
