package controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import domain.Order;
import domain.Product;
import service.Service;

public class Runner {
	
	public static void main(String[] args) {
		try (Service service = Service.getService()){
			//addProduct(service);
			//listAllProducts(service);
			//addOrder(service);
			//listAllOrders(service);
			//printProductById(service, 3);
			//printProductByIds(service, List.of(1L,2L,3L));
			//printOrderById(service, 13);
			//updateOrderQuantities(service);
			printOrderedProductsTotalQuantityDescending(service);
		}			
		
	}
	
	private static void updateOrderQuantities(Service service) {
		Optional<Order> order = service.getOrderById(13);
		Map<Product,Integer> quantities=new HashMap<>();
		Optional<Product> product1 = service.getProductById(1);
		product1.ifPresent(p->quantities.put(p,7));
		Optional<Product> product2 = service.getProductById(2);
		product2.ifPresent(p->quantities.put(p,4));
		
		if(order.isPresent()) {
			service.updateOrderEntryQuantities(order.get(),quantities);			
		}
	}
	
	private static void printOrderById(Service service, long id) {
		Optional<Order> order = service.getOrderById(id);
		order.ifPresentOrElse(System.out::println, ()->System.out.printf("no order found for id %d%n", id));
	}

	private static void printProductByIds(Service service, Iterable<Long> ids) {
		for(Product product:service.getProductByIds(ids)) {
			System.out.println(product);
		}
	}

	private static void printProductById(Service service, long id) {
		Optional<Product> product = service.getProductById(id);
		product.ifPresentOrElse(System.out::println, ()->System.out.printf("no product found for id %d%n", id));
	}

	private static void printOrderedProductsTotalQuantityDescending(Service service) {
		for(Object[] row:service.getOrderedProductsTotalQuantityDescending()) {
			for(Object value:row) {
				System.out.print(value);
				System.out.print(' ');
			}
			System.out.println();
		}		
	}

	private static void listAllOrders(Service service) {
		for(var order:service.getAllOrders()) {
			System.out.println(order);
		}
	}

	private static void addOrder(Service service) {
		List<Product> products = new LinkedList<>();
		Optional<Product> product1 = service.getProductById(3);
		product1.ifPresent(products::add);
		Optional<Product> product2 = service.getProductById(1);
		product2.ifPresent(products::add);
		System.out.println(products);
		
		Order order = new Order(); 
		order.setStatus(Order.Status.SHIPPED);
		order.setCreatedAt(LocalDateTime.now()); 
		service.createOrder(order,products); 
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
		productA.setName("Cauldron");
		productA.setPrice(BigDecimal.valueOf(25690, 2));
		productA.setStatus(Product.Status.OUT_OF_STOCK);
		service.createProduct(productA);
		System.out.println(productA);
	}
	
}
