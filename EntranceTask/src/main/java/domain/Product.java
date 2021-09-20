package domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product implements Serializable {
	
	public enum Status{ OUT_OF_STOCK, IN_STOCK, RUNNING_LOW};
	
	@Id @GeneratedValue private long id;
	private String name;
	private BigDecimal price;
	private Status status;
	@Column(name = "created_at") private LocalDateTime createdAt;
	
	public long getId() {
		return id;
	}
	
	protected void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		if(price.signum()<1) throw new IllegalArgumentException("price should be positive value");
		this.price = price;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Product product) {
			return Objects.equals(id, product.id);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return new StringJoiner(",","[","]")
				.add(name)
				.add(price.toString())
				.add(status.toString())
				.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(createdAt))
				.toString();
	}
	
}
