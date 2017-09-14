package com.example.thele.expired;
/*
 * Item.java
 *
 *  Created on: Mar 27, 2017
 *      Author: thele
 *  Dates are stored via "yyyyMMdd"
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Item
{
	private String name;
	private String datePurchased;
	private String dateExpired;
	private String storageType;

	// Constructors
	public Item(String name, String storageType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
		this.name = name;
		this.datePurchased = sdf.format(cal.getTime());
		this.dateExpired = "";
		this.storageType = storageType;
	}

	public Item(String name, String dateExpired, String storageType) {
		this.name = name;
		this.datePurchased = "";
		this.dateExpired = dateExpired;
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
	public void setName(String name) { this.name = name; };
	public void setDatePurchased(String datePurchased) { this.datePurchased = datePurchased; };
	public void setDateExpired(String dateExpired) { this.dateExpired = dateExpired; };
	public void setStorageType(String storageType) { this.storageType = storageType; };

	// change storage type and its respective expiration date
	void changeType(String name)
	{

	}
}