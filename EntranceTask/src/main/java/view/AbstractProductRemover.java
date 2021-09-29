package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;

import service.Service;

public abstract class AbstractProductRemover extends Executor {

	private final int passwordDigest;

	protected AbstractProductRemover(Service service, DataInput source, PrintWriter dest, int passwordDigest) {
		super(service,source,dest);
		this.passwordDigest = passwordDigest;
	}
	
	public abstract Object action() throws IOException;

	@Override
	public Object perform(Object arg) throws Exception {
		if(passwordDigest!=getPasswordDigest()) throw new IllegalArgumentException("wrong password"); 
		return action(); 
	}

	private int getPasswordDigest() throws IOException {
		getDest().printf("Please enter password: ");
		return getSource().readLine().hashCode();
	}

}
