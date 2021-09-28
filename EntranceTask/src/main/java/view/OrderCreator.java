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

public class OrderCreator extends Executor {

	public OrderCreator(Service service, DataInput source, PrintWriter dest) {
		super(service,source,dest);
	}

	@Override
	public Object perform(Object arg) throws Exception {
		Order order = new Order();
		order.setStatus(getStatus());
		final List<Product> products = getProducts();
		order.setCreatedAt(LocalDateTime.now());
		getService().createOrder(order,products); 
		return order;
	}

	private Status getStatus() throws IOException {
		getDest().printf("Please enter ORDERED, APPROVED, NOT_APPROVED, SHIPPED, DELIVERED, UNABLE_TO_DELIVER, or CANCELED: ");
		return Enum.valueOf(Order.Status.class, getSource().readLine().strip().toUpperCase());
	}
	
	private List<Product> getProducts() throws IOException {
		getDest().printf("Please enter list of product ids for this order: ");
		List<Product> products = new LinkedList<>();
		try(Scanner scanner = new Scanner(getSource().readLine())){
			while(scanner.hasNextLong()) {
				getService().getProductById(scanner.nextLong()).ifPresent(products::add);
			}			
		}
		return products;
	}

}
