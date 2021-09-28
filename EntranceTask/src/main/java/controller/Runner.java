package controller;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import domain.Order;
import domain.Product;
import service.Service;
import view.AllProductsRemover;
import view.ListAllViewer;
import view.Menu;
import view.OrderCreator;
import view.OrderQuantitiesUpdater;
import view.ProductCreator;
import view.ProductRemover;
import view.Target;

public class Runner {
	
	private static final String QUIT_OPTION = "q";
	
	private static final String PASSWORD_KEY = "password";
	
	private static final int getPasswordHashCode(Service service) throws Exception {
		return service.getProperties().getProperty(PASSWORD_KEY).hashCode();
	}
	
	private static void printPrompt(PrintWriter dest, Menu menu) {
		dest.printf("Please type appropriate key and press Enter to select menu option or \'%s\' to quit%n%s%n%n", QUIT_OPTION, menu);
	}
	
	private static void printGoodbye(PrintWriter dest) {
		dest.printf("Bye!%n");
	}
	
	private static String readInput(DataInput source) throws IOException {
		return source.readLine();
	}
	
	private static void run(DataInput source, PrintWriter dest, Menu menu) {
		try {
			while(true) {
				printPrompt(dest, menu);
				String response = readInput(source);
				if(response.equals(QUIT_OPTION)) {
					printGoodbye(dest);
					break;
				}
				try {
					Optional<Object> result = menu.act(response, response);
					if(result.isPresent()) {
						dest.format("%nSuccessfully done.%nGot result: %s%n%n",result.get());						
					} else {
						dest.format("%nOK.%n%n");												
					}
				}catch(Exception e) {
					dest.format("%nError: %s%n%n",e.getLocalizedMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		final DataInput source = new DataInputStream(System.in);
		final PrintWriter dest = new PrintWriter(System.out, true);
		
		final Target reporter = (Object x)->{dest.printf("argument %s received.%n%n",x); return null;};
		
		try (Service service = Service.getService()){
			final Menu menu = new Menu().
					add("1", "Create product", new ProductCreator(service,source,dest)).
					add("2", "Create order", new OrderCreator(service,source,dest)).
					add("3", "Update order quantities", new OrderQuantitiesUpdater(service,source,dest)).
					add("4", "List all products", new ListAllViewer(service,source,dest,service::getAllProducts)).
					add("5", "List all ordered products total quantity sorted desc", reporter).
					add("6", "Print selected order", reporter).
					add("7", "List all orders", reporter).
					add("8", "Remove product", new ProductRemover(service,source,dest,getPasswordHashCode(service))).
					add("9", "Remove all products", new AllProductsRemover(service,source,dest,getPasswordHashCode(service)));
			
			run(source, dest, menu);

			//printProductById(service, 3);
			//printProductByIds(service, List.of(1L,2L,3L));
			//printOrderById(service, 11);
			//printOrderedProductsTotalQuantityDescending(service);
			//printAllOrderEntries(service);
			//printOneOrderEntries(service,12L);
		} catch(Exception e) {
			e.printStackTrace();
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

	private static void printTuples(Supplier<List<Object[]>> producer) {
		for(Object[] row:producer.get()) {
			for(Object value:row) {
				System.out.printf("%s ", value);
			}
			System.out.println();
		}
	}
	
	private static void printTuples(List<Object[]> data) {
		for(Object[] row:data) {
			for(Object value:row) {
				System.out.printf("%s ", value);
			}
			System.out.println();
		}
	}
	
	private static void printOrderedProductsTotalQuantityDescending(Service service) {
		printTuples(service::getOrderedProductsTotalQuantityDescending);
	}
	
	private static void printAllOrderEntries(Service service) {
		printTuples(service::getAllOrderEntries);
	}

	private static void printOneOrderEntries(Service service,long id) {
		service.getOrderById(id).ifPresent(order->printTuples(service.getOrderEntriesBy(order)));
	}

}
