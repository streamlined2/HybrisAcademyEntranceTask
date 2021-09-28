package view;

import java.io.DataInput;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Supplier;

import service.Service;

public class AllTupleViewer extends TupleViewer {

	private Supplier<List<Object[]>> supplier;

	public AllTupleViewer(Service service, DataInput source, PrintWriter dest, Supplier<List<Object[]>> supplier) {
		super(service, source, dest);
		this.supplier = supplier;
	}

	@Override
	protected List<Object[]> query() {
		return supplier.get();
	}
	
}
