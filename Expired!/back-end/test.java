/*
 * test.java
 *
 *  Created on: Mar 27, 2017
 *      Author: thele
 */

public class test {
	public static void main(String args[])
	{
		Fridge fridge = new Fridge();
		fridge.readExpired("expdates.txt");

		// create items
		/*
		Item item1 = new Item("Carrot", "20170327", "20170330");
		Item item2 = new Item("Cabbage", "20170329", "20170430");
		Item item3 = new Item("Beef", "20170330", "20170410");
		Item item4 = new Item("Bread", "20170329", "20170330");
		*/
		Item item5 = new Item("Beef", "20170330");

		// add items
		/*
		fridge.add(item1);
		fridge.add(item2);
		fridge.add(item3);
		fridge.add(item4);
		*/
		fridge.add(item5);

		// print items
		fridge.printContents(5);
		System.out.println("");
		fridge.printRecent(5);
		System.out.println("");
	}
}