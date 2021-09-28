package view;

import java.io.DataInput;
import java.io.PrintWriter;
import java.util.List;
import service.Service;

public abstract class TupleViewer extends Executor {
	
	protected TupleViewer(Service service, DataInput source, PrintWriter dest) {
		super(service, source, dest);
	}
	
	protected abstract List<Object[]> query() throws Exception;

	@Override
	public Object perform(Object arg) throws Exception {
		int cnt = 0;
		for(Object[] row:query()) {
			for(Object value:row) {
				getDest().printf("%s ", value);
			}
			cnt++;
			getDest().println();
		}
		return Integer.valueOf(cnt); 
	}

}
