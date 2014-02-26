package com.foodtreasure.account.treasuremanager.db;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewOrder {

	private Customer customer;
	HashMap<Long, ArrayList<Dishes>> orders;
	
	public ViewOrder() {
		orders = new HashMap<Long, ArrayList<Dishes>>();
	}
	
	public ViewOrder(Customer customer) {
		this();
		this.customer = customer;
	}
	
	public ViewOrder(Customer customer, HashMap<Long, ArrayList<Dishes>> orders) {
		this(customer);
		this.orders = orders;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void addDishesToOrder(Long orderId, ArrayList<Dishes> dishesInOrder) {
		if (dishesInOrder == null || dishesInOrder.size() == 0) {
			return;
		}
		
		ArrayList<Dishes> storedDishes = orders.get(orderId);
		if (storedDishes == null) {
			// No dishes stored already
			orders.put(orderId, dishesInOrder);
		} else {
			for (int i = 0; i < dishesInOrder.size(); i++) {
				storedDishes.add(dishesInOrder.get(i));
			}
		}
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public HashMap<Long, ArrayList<Dishes>> getOrders() {
		return orders;
	}
	
	public static class Dishes {
		
		private Menu dish;
		private int number;
		
		public Dishes(Menu dish, int number) {
			this.dish = dish;
			this.number = number;
		}
		
		public Menu getDish() {
			return dish;
		}
		
		public int getNumberOfDish() {
			return number;
		}
		
		public void setDish(Menu dish) {
			this.dish = dish;
		}
		
		public void setNumberOfDish(int number) {
			this.number = number;
		}
	}
}
