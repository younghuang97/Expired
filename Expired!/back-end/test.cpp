/*
 * test.cpp
 *
 *  Created on: Mar 27, 2017
 *      Author: thele
 */
#include <iostream>
#include "Fridge.h"
#include "stdafx.h"

int main(int argc, char** argv)
{
	Fridge* fridge = new Fridge();

	// create items
	Item* item1 = new Item("Carrot", "20170327", "20170330");
	Item* item2 = new Item("Cabbage", "20170329", "20170430");
	Item* item3 = new Item("Beef", "20170330", "20170410");
	Item* item4 = new Item("Bread", "20170329", "20170330");

	// add items
	fridge->add(item1);
	fridge->add(item2);
	fridge->add(item3);
	fridge->add(item4);

	// print items
	fridge->printContents(5);
	cout << endl;
	fridge->printRecent(5);
	cout << endl;
	return 0;
}
