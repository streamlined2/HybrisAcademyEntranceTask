package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

	public enum Status { ORDERED, APPROVED, NOT_APPROVED, SHIPPED, DELIVERED, UNABLE_TO_DELIVER, CANCELED}

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;

	@Column(name = "user_id") @Generated(GenerationTime.ALWAYS) 
	private long user;
	
	@Column(name = "status") @Convert(converter = OrderStatusAttributeConverter.class)
	private Status status;
	
	@Column(name = "created_at") @Convert(attributeName = "created_at", converter = LocalDateTimeAttributeConverter.class) 
	private LocalDateTime createdAt;
	
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}
	
	public long getUser() {
		return user;
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
		if(o instanceof Order order) {
			return Objects.equals(id, order.id);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return new StringJoiner(",","[","]")
				.add(Long.toString(id))
				.add(Long.toString(user))
				.add(status.toString())
				.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(createdAt))
				.toString();
	}
	
}
