/*
 * Item.java
 *
 *  Created on: Mar 27, 2017
 *      Author: thele
 */

public class Item
{
	private String name;
	private String datePurchased;
	private String dateExpired;
	/*
	 * Sorts Items by their name field, used for accessing items in an
	 * unordered_map.
	 */
	//bool operator<(const Item& item) const;
	// Constructors
	public Item(String name, String datePurchased) {
        this.name = name;
        this.datePurchased = datePurchased;
        this.dateExpired = "";
    }
	public Item(String name, String datePurchased, String dateExpired) {
        this.name = name;
        this.datePurchased = datePurchased;
        this.dateExpired = dateExpired;
    }
	// Accessors
	public String getName() { return name; };
	public String getDatePurchased() { return datePurchased; };
	public String getDateExpired() { return dateExpired; };
	public void setDateExpired(String dateExpired) { this.dateExpired = dateExpired; };
}

/*bool Item::operator<(const Item& item) const
{
	int value = this->getName().compare(item.getName());
	if (value < 0) return true;
	else return false;
}*/