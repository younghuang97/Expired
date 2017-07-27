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
		fridge.readDatabase();
		fridge.readList();

		// create items
		Item item1 = new Item("Carrot", "20170327", "20170330", "fridge");
		Item item2 = new Item("Cabbage", "20170329", "freeze");
		Item item3 = new Item("Beef", "20170330", "20170401", "fridge");
		Item item4 = new Item("Bread", "20170329", "freeze");

		// add items
		fridge.add(item1);
		fridge.add(item2);
		fridge.add(item3);
		fridge.add(item4);

		fridge.writeList();

		// debug use
		/*
		fridge.printDatabase();
		System.out.println("");
		*/

		// print items
		System.out.println("Items sorted by date expired:");
		fridge.printDateExpired(5);
		System.out.println("");
		System.out.println("Items sorted by date purchased:");
		fridge.printDatePurchased(5);
		System.out.println("");
	}
}