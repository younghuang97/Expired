/*
 * Item.cpp
 *
 *  Created on: Mar 27, 2017
 *      Author: thele
 */

#include "Item.h"
#include "stdafx.h"

using namespace std;

bool Item::operator<(const Item& item) const
{
	int value = this->getName().compare(item.getName());
	if (value < 0) return true;
	else return false;
}
