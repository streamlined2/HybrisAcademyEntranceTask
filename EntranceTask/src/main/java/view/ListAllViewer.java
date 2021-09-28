package view;

import java.io.DataInput;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

import service.Service;

public class ListAllViewer extends EntityViewer {
	
	private final Supplier<List<? extends Serializable>> supplier;
	
	public ListAllViewer(Service service, DataInput source, PrintWriter dest, Supplier<List<? extends Serializable>> producer) {
		super(service,source,dest);
		this.supplier = producer;
	}

	@Override
	public List<? extends Serializable> query() {
		return supplier.get();
	}

}
