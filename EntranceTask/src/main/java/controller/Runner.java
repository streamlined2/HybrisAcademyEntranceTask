package controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import domain.Order;
import domain.Product;
import service.Service;

public class Runner {
	
	public static void main(String[] args) {
		try (Service service = Service.getService()){
			//addProduct(service);
			//listAllProducts(service);
			addOrder(service);
			listAllOrders(service);
		}			
		
	}

	private static void listAllOrders(Service service) {
		for(var order:service.getAllOrders()) {
			System.out.println(order);
		}
	}

	private static void addOrder(Service service) {
		List<Product> products = service.getAllProducts();
		Order order = new Order();
		order.setStatus(Order.Status.SHIPPED);
		order.setCreatedAt(LocalDateTime.now());
		service.createOrder(order, products);
		System.out.println(order);
	}

	private static void listAllProducts(Service service) {
		for(var product:service.getAllProducts()) {
			System.out.println(product);
		}
	}

	private static void addProduct(Service service) {
		Product productA = new Product();
		productA.setCreatedAt(LocalDateTime.now());
		productA.setName("Kettle");
		productA.setPrice(BigDecimal.valueOf(12534, 2));
		productA.setStatus(Product.Status.IN_STOCK);
		service.createProduct(productA);
		System.out.println(productA);
	}
	
}
