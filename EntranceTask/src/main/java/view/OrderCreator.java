package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import domain.Order;
import domain.Order.Status;
import domain.Product;
import service.Service;

public class OrderCreator implements Target {

	private final Service service;
	private final DataInput source;
	private final PrintWriter dest;

	public OrderCreator(Service service, DataInput source, PrintWriter dest) {
		this.service = service;
		this.source = source;
		this.dest = dest;
	}

	@Override
	public Object perform(Object arg) throws Exception {
		Order order = new Order();
		order.setStatus(getStatus());
		final List<Product> products = getProducts();
		order.setCreatedAt(LocalDateTime.now());
		service.createOrder(order,products); 
		return order;
	}

	private Status getStatus() throws IOException {
		dest.printf("Please enter ORDERED, APPROVED, NOT_APPROVED, SHIPPED, DELIVERED, UNABLE_TO_DELIVER, or CANCELED: ");
		return Enum.valueOf(Order.Status.class, source.readLine().strip().toUpperCase());
	}
	
	private List<Product> getProducts() throws IOException {
		dest.printf("Please enter list of product ids for this order: ");
		List<Product> products = new LinkedList<>();
		try(Scanner scanner = new Scanner(source.readLine())){
			while(scanner.hasNextLong()) {
				service.getProductById(scanner.nextLong()).ifPresent(products::add);
			}			
		}
		return products;
	}

}
