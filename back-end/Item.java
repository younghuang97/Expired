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
	private String storageType;

	// Constructors
	public Item(String name, String datePurchased, String storageType) {
		this.name = name;
		this.datePurchased = datePurchased;
		this.dateExpired = "";
		this.storageType = storageType;
	}

	public Item(String name, String datePurchased, String dateExpired, String storageType) {
        this.name = name;
        this.datePurchased = datePurchased;
        this.dateExpired = dateExpired;
        this.storageType = storageType;
    }

	// Accessors
	public String getName() { return name; };
	public String getDatePurchased() { return datePurchased; };
	public String getDateExpired() { return dateExpired; };
	public String getStorageType() { return storageType; };

	// Mutators
	public void setDateExpired(String dateExpired) { this.dateExpired = dateExpired; };

	// change storage type and its respective expiration date
	void changeType(String name)
	{

	}
}