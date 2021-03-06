package domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

class OrderEntryPK {
	private Order order;
	private Product product;
}

@Entity
@Table(name = "order_items")
@IdClass(OrderEntryPK.class)
public class OrderEntry implements Serializable {
	
	@Id @ManyToOne @JoinColumn 
	private Order order;
	
	@Id @ManyToOne @JoinColumn
	private Product product;
	
	private int quantity;
	
	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		if(quantity<=0) throw new IllegalArgumentException("quantity should be positive number");
		this.quantity = quantity;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(order,product);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof OrderEntry item) {
			return Objects.equals(order, item.order) && Objects.equals(product, item.product);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return new StringJoiner(",","[","]")
				.add(order.toString())
				.add(product.toString())
				.add(Integer.toString(quantity))
				.toString();
	}
	
}
