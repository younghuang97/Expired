/*
 * Item.h
 *
 *  Created on: Mar 21, 2017
 *      Author: thele
 */

#ifndef ITEM_H_
#define ITEM_H_

#include <string>

using namespace std;

class Item
{
private:
	const string name;
	const string datePurchased;
	const string dateExpired;
	/*
	 * Sorts Items by their name field, used for accessing items in an
	 * unordered_map.
	 */
	bool operator<(const Item& item) const;
public:
	// Constructors
	Item(string name, string datePurchased) :
		name(name), datePurchased(datePurchased), dateExpired(0) {}
	Item(string name, string datePurchased, string dateExpired) :
		name(name), datePurchased(datePurchased), dateExpired(dateExpired) {}
	// Accessors
	const string& getName() const { return name; };
	const string& getDatePurchased() const { return datePurchased; };
	const string& getDateExpired() const { return dateExpired; };
};

#endif /* ITEM_H_ */
