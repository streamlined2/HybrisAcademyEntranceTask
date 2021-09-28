package view;

import java.io.DataInput;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import domain.Order;
import service.Service;

public class OneTupleViewer<E extends Serializable> extends TupleViewer {

	private Function<E,List<Object[]>> supplier;

	public OneTupleViewer(Service service, DataInput source, PrintWriter dest, Function<E,List<Object[]>> supplier) {
		super(service, source, dest);
		this.supplier = supplier;
	}

	@Override
	protected List<Object[]> query() throws Exception {
		long orderId = getOrderId();
		Optional<Order> order = getService().getOrderById(orderId);
		if(order.isPresent()) {
			return getService().getOrderEntriesBy(order.get());
		}
		throw new NoSuchElementException(String.format("no order found with id %d",orderId));
	}

	private long getOrderId() throws IOException {
		getDest().printf("Please enter order id: ");
		return Long.parseLong(getSource().readLine());
	}
	
}
