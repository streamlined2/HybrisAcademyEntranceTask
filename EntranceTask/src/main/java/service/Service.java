package service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.Order;
import domain.Product;

public class Service {
	
	public Product createProduct() {
		return null;//TODO
	}
	
	public Order createOrder(Product... products) {
		return null;//TODO
	}
	
	public void updateOrderEntryQuantities(Map<Product,Integer> quantities) {
		//TODO
	}
	
	public Set<Product> getAllProducts(){
		return null;//TODO
	}
	
	public Set<Product> getOrderedProductsTotalQuantityDescending(){
		return null;//TODO
	}
	
	public List<?> getOrderEntriesBy(Order order){
		return null;//TODO
	}
	
	public List<?> getAllOrderEntries(){
		return null;//TODO
	}
	
	public boolean removeProduct(Product product) {
		return false;//TODO
	}
	
	public void removeAllProducts() {
		//TODO
	}

}
