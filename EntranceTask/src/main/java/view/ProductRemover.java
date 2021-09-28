package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import service.Service;

public class ProductRemover extends Executor {
	
	private final int passwordHashCode;

	public ProductRemover(Service service, DataInput source, PrintWriter dest, int passwordHashCode) {
		super(service,source,dest);
		this.passwordHashCode = passwordHashCode;
	}

	@Override
	public Object perform(Object arg) throws Exception {
		if(passwordHashCode==getPasswordHashCode()) {
			return getService().removeProduct(getService().getProductById(getProductId()).orElseThrow());
		}
		throw new IllegalArgumentException("wrong password"); 
	}

	private long getProductId() throws IOException {
		getDest().printf("Please enter product id: ");
		return Long.parseLong(getSource().readLine());
	}
	
	private int getPasswordHashCode() throws IOException {
		getDest().printf("Please enter password: ");
		return getSource().readLine().hashCode();
	}

}
