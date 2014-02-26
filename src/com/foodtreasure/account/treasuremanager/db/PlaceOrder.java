package com.foodtreasure.account.treasuremanager.db;

import java.util.ArrayList;

public class PlaceOrder {

	private String name;
	private String phone;
	private String price;
	private long date; // Time in millis when order has to be processed/delivered and not when it is received
	private ArrayList<Dishes> dishes;
	
	public PlaceOrder() {
		dishes = new ArrayList<Dishes>();
	}
	
	public PlaceOrder(String name, String phone, String price, long millis) {
		this();
		this.name = name;
		this.phone = phone;
		this.price = price;
		this.date = millis;
	}

	/**
	 * Adds Dishes to order
	 * @param dishesToAdd
	 */
	public void addDishesToOrder(ArrayList<Dishes> dishesToAdd) {
		if (dishesToAdd == null || dishesToAdd.size() == 0) {
			return;
		}
		
		if (dishes.size() == 0) {
			dishes = dishesToAdd;
		} else {
			for (int i = 0; i < dishesToAdd.size(); i++) {
				dishes.add(dishesToAdd.get(i));
			}
		}
	}
	
	public void addDishToOrder(Menu dish, int num_items) {
		dishes.add(new Dishes(dish, num_items));
	}
	
	public void setCustomerName(String name) {
		this.name = name;
	}
	
	public void setCustomerPhone(String phone) {
		this.phone = phone;
	}
	
	public void setPriceOfOrder(String price) {
		this.price = price;
	}
	
	public void setOrderDate(long date) {
		this.date = date;
	}
	
	public String getCustomerName() {
		return name;
	}
	
	public String getCustomerPhone() {
		return phone;
	}
	
	public String getPriceOfOrder() {
		return price;
	}
	
	public long getOrderDate() {
		return date;
	}
	
	public ArrayList<Dishes> getDishesInOrder() {
		return dishes;
	}
	
	public static class Dishes {
		
		private Menu dish;
		private int numItems;

		public Dishes(Menu dish, int num_items) {
			this.dish = dish;;
			numItems = num_items;
		}
		
		public Menu getDish() {
			return dish;
		}
		
		public int getNumItems() {
			return numItems;
		}

		public void setDish(Menu dish) {
			this.dish = dish;
		}
		
		public void setNumItems(int numItems) {
			this.numItems = numItems;
		}
	}
}
