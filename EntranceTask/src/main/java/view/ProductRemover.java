package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import service.Service;

public class ProductRemover extends AbstractProductRemover {
	
	public ProductRemover(Service service, DataInput source, PrintWriter dest, int passwordHashCode) {
		super(service,source,dest,passwordHashCode);
	}

	@Override
	public Object action() throws IOException {
		return getService().removeProduct(getService().getProductById(getProductId()).orElseThrow());
	}

	private long getProductId() throws IOException {
		getDest().printf("Please enter product id: ");
		return Long.parseLong(getSource().readLine());
	}
	
}
