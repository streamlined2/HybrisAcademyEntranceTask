package view;

import java.io.DataInput;
import java.io.PrintWriter;

import service.Service;

public class AllProductsRemover extends AbstractProductRemover {

	public AllProductsRemover(Service service, DataInput source, PrintWriter dest, int passwordHashCode) {
		super(service,source,dest,passwordHashCode);
	}

	@Override
	public Object action() {
		return Integer.valueOf(getService().removeAllProducts());
	}
	
}
