package view;

import java.io.DataInput;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Supplier;

import service.Service;

public class TupleViewer extends Executor {
	
	private Supplier<List<Object[]>> supplier;

	public TupleViewer(Service service, DataInput source, PrintWriter dest, Supplier<List<Object[]>> supplier) {
		super(service, source, dest);
		this.supplier = supplier;
	}

	@Override
	public Object perform(Object arg) throws Exception {
		int cnt = 0;
		for(Object[] row:supplier.get()) {
			for(Object value:row) {
				getDest().printf("%s ", value);
			}
			cnt++;
			getDest().println();
		}
		return Integer.valueOf(cnt); 
	}

}
