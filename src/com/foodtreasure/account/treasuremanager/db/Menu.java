/**
 * 
 */
package com.foodtreasure.account.treasuremanager.db;

/**
 * @author agupta
 *
 */
public class Menu {

	private long menuId;
	private int dishId;
	private int quantityId;
	private String price;
	
	public Menu(long menu_id, int dishId, int quantityId, String price) {
		this.menuId = menu_id;
		this.dishId = dishId;
		this.quantityId = quantityId;
		this.price = price;
	}
	
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
	
	public void setDishId(int dishId) {
		this.dishId = dishId;
	}
	
	public void setQuantityId(int quantityId) {
		this.quantityId = quantityId;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public long getMenuId() {
		return menuId;
	}
	
	public int getDishId() {
		return dishId;
	}
	
	public int getQuantityId() {
		return quantityId;
	}
	
	public String getPrice() {
		return price;
	}
	
	public String getDishName() {
		return DatabaseUtils.getInstance().getDishNameById(dishId);
	}
	
	public String getQuantity() {
		return DatabaseUtils.getInstance().getQuantityNameById(quantityId);
	}
}
