package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import domain.Order;
import domain.Product;
import service.Service;

public class OrderQuantitiesUpdater implements Target {

	private final Service service;
	private final DataInput source;
	private final PrintWriter dest;

	public OrderQuantitiesUpdater(Service service, DataInput source, PrintWriter dest) {
		this.service = service;
		this.source = source;
		this.dest = dest;
	}

	@Override
	public Object perform(Object arg) throws Exception {
		long orderId = getOrderId();
		Optional<Order> order = service.getOrderById(orderId);
		if(order.isPresent()) {
			Map<Product,Integer> quantities=getProductIdsQuantities();
			service.updateOrderEntryQuantities(order.get(),quantities);			
			return quantities;
		}
		throw new IllegalArgumentException(String.format("no order found with id %d",orderId));
	}
	
	private long getOrderId() throws IOException {
		dest.printf("Please enter order id: ");
		return Long.parseLong(source.readLine());
	}
	
	private Map<Product, Integer> getProductIdsQuantities() throws IOException {
		Map<Product,Integer> quantities = new LinkedHashMap<>();
		dest.printf("Please enter product ids: ");
		try(Scanner scanner = new Scanner(source.readLine())){
			while(scanner.hasNextLong()) {
				service.getProductById(scanner.nextLong()).ifPresent(product->quantities.put(product,1));
			}
			dest.printf("Please enter quantity for each product: ");
			try(Scanner scanner2 = new Scanner(source.readLine())){
				for(var product:quantities.keySet()) {
					if(!scanner2.hasNextInt()) break;
					quantities.put(product, scanner2.nextInt());
				}
			}
		}
		return quantities;
	}

}
