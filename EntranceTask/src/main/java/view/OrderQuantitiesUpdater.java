package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import domain.Order;
import domain.Product;
import service.Service;

public class OrderQuantitiesUpdater extends Executor {

	public OrderQuantitiesUpdater(Service service, DataInput source, PrintWriter dest) {
		super(service,source,dest);
	}

	@Override
	public Object perform(Object arg) throws Exception {
		long orderId = getOrderId();
		Optional<Order> order = getService().getOrderById(orderId);
		if(order.isPresent()) {
			Map<Product,Integer> quantities=getProductIdsQuantities();
			getService().updateOrderEntryQuantities(order.get(),quantities);			
			return quantities;
		}
		throw new NoSuchElementException(String.format("no order found with id %d",orderId));
	}
	
	private long getOrderId() throws IOException {
		getDest().printf("Please enter order id: ");
		return Long.parseLong(getSource().readLine());
	}
	
	private Map<Product, Integer> getProductIdsQuantities() throws IOException {
		Map<Product,Integer> quantities = new LinkedHashMap<>();
		getDest().printf("Please enter product ids: ");
		try(Scanner scanner = new Scanner(getSource().readLine())){
			while(scanner.hasNextLong()) {
				getService().getProductById(scanner.nextLong()).ifPresent(product->quantities.put(product,1));
			}
			getDest().printf("Please enter quantity for each product: ");
			try(Scanner scanner2 = new Scanner(getSource().readLine())){
				for(var product:quantities.keySet()) {
					if(!scanner2.hasNextInt()) break;
					quantities.put(product, scanner2.nextInt());
				}
			}
		}
		return quantities;
	}

}
