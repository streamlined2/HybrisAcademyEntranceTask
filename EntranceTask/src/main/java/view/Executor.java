package view;

import java.io.DataInput;
import java.io.PrintWriter;

import service.Service;

public abstract class Executor implements Target {

	private final Service service;
	private final DataInput source;
	private final PrintWriter dest;

	protected Executor(Service service, DataInput source, PrintWriter dest) {
		this.service = service;
		this.source = source;
		this.dest = dest;
	}
	
	protected Service getService() { return service;}
	protected DataInput getSource() { return source;}
	protected PrintWriter getDest() { return dest;}

}
