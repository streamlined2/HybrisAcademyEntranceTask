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
	public Object accept(Object arg) throws Exception {
		Product productA = new Product();
		productA.setCreatedAt(LocalDateTime.now());
		productA.setName(getName());
		productA.setPrice(getPrice());
		productA.setStatus(getStatus());
		service.createProduct(productA);
		return productA;
	}

	private Status getStatus() throws IOException {
		dest.print("Please enter OUT_OF_STOCK, IN_STOCK, or RUNNING_LOW: ");
		return Enum.valueOf(Product.Status.class, source.readLine().strip().toUpperCase());
	}

	private BigDecimal getPrice() throws NumberFormatException, IOException {
		dest.print("Please enter product price: ");
		return BigDecimal.valueOf(Double.parseDouble(source.readLine()));
	}

	private String getName() throws IOException {
		dest.print("Please enter product name: ");
		return source.readLine();
	}

}
