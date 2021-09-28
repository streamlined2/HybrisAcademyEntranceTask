package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import domain.Product;
import domain.Product.Status;
import service.Service;

public class ProductCreator extends Executor {
	
	public ProductCreator(Service service, DataInput source, PrintWriter dest) {
		super(service,source,dest);
	}

	@Override
	public Object perform(Object arg) throws Exception {
		Product product = new Product();
		product.setName(getName());
		product.setPrice(getPrice());
		product.setStatus(getStatus());
		product.setCreatedAt(LocalDateTime.now());
		getService().createProduct(product);
		return product;
	}

	private Status getStatus() throws IOException {
		getDest().printf("Please enter OUT_OF_STOCK, IN_STOCK, or RUNNING_LOW: ");
		return Enum.valueOf(Product.Status.class, getSource().readLine().strip().toUpperCase());
	}

	private BigDecimal getPrice() throws NumberFormatException, IOException {
		getDest().printf("Please enter product price: ");
		return BigDecimal.valueOf(Double.parseDouble(getSource().readLine()));
	}

	private String getName() throws IOException {
		getDest().printf("Please enter product name: ");
		return getSource().readLine();
	}

}
