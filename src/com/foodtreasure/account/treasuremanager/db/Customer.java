package com.foodtreasure.account.treasuremanager.db;

public class Customer {

	private long id;
	private String name;
	private String phone;
	private double balance;
	
	public Customer () {
		
	}
	
	public Customer(String name, String phone, double balance) {
		this();
		this.name = name;
		this.phone = phone;
		this.balance = balance;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public long getId() {
		return id;
	}
}
