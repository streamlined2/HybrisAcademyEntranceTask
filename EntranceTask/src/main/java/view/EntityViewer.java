package view;

import java.io.DataInput;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import service.Service;

public abstract class EntityViewer extends Executor {

	protected EntityViewer(Service service, DataInput source, PrintWriter dest) {
		super(service,source,dest);
	}
	
	public abstract List<? extends Serializable> query();

	@Override
	public Object perform(Object arg) throws Exception {
		int cnt = 0;
		for(var entity:query()) {
			getDest().println(entity);
			cnt++;
		}
		return Integer.valueOf(cnt); 
	}

}
