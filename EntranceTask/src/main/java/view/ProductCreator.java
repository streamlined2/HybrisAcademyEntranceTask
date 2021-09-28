package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import domain.Product;
import domain.Product.Status;
import service.Service;

public class ProductCreator implements Target {
	
	private final Service service;
	private final DataInput source;
	private final PrintWriter dest;

	public ProductCreator(Service service, DataInput source, PrintWriter dest) {
		this.service = service;
		this.source = source;
		this.dest = dest;
	}

	@Override
	public Object perform(Object arg) throws Exception {
		Product product = new Product();
		product.setCreatedAt(LocalDateTime.now());
		product.setName(getName());
		product.setPrice(getPrice());
		product.setStatus(getStatus());
		service.createProduct(product);
		return product;
	}

	private Status getStatus() throws IOException {
		dest.printf("Please enter OUT_OF_STOCK, IN_STOCK, or RUNNING_LOW: ");
		return Enum.valueOf(Product.Status.class, source.readLine().strip().toUpperCase());
	}

	private BigDecimal getPrice() throws NumberFormatException, IOException {
		dest.printf("Please enter product price: ");
		return BigDecimal.valueOf(Double.parseDouble(source.readLine()));
	}

	private String getName() throws IOException {
		dest.printf("Please enter product name: ");
		return source.readLine();
	}

}
