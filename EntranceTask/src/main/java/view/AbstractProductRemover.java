package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;

import service.Service;

public abstract class AbstractProductRemover extends Executor {

	private final int passwordHashCode;

	protected AbstractProductRemover(Service service, DataInput source, PrintWriter dest, int passwordHashCode) {
		super(service,source,dest);
		this.passwordHashCode = passwordHashCode;
	}
	
	public abstract Object action() throws IOException;

	@Override
	public Object perform(Object arg) throws Exception {
		if(passwordHashCode!=getPasswordHashCode()) throw new IllegalArgumentException("wrong password"); 
		return action(); 
	}

	private int getPasswordHashCode() throws IOException {
		getDest().printf("Please enter password: ");
		return getSource().readLine().hashCode();
	}

}
