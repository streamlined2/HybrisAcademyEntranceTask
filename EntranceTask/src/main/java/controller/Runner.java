package controller;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import service.Service;
import view.AllProductsRemover;
import view.ListAllViewer;
import view.Menu;
import view.OrderCreator;
import view.OrderQuantitiesUpdater;
import view.ProductCreator;
import view.ProductRemover;
import view.Target;
import view.TupleViewer;

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
					add("5", "List all ordered products total quantity sorted desc", new TupleViewer(service,source,dest,service::getOrderedProductsTotalQuantityDescending)).
					add("6", "Print selected order", reporter).
					add("7", "List all orders", reporter).
					add("8", "Remove product", new ProductRemover(service,source,dest,getPasswordHashCode(service))).
					add("9", "Remove all products", new AllProductsRemover(service,source,dest,getPasswordHashCode(service)));
			
			run(source, dest, menu);

		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
