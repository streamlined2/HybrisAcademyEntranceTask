package domain;

import java.io.Serializable;
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
@Table(name = "orders")
public class Order implements Serializable {

	public enum Status { ORDERED, APPROVED, NOT_APPROVED, SHIPPED, DELIVERED, UNABLE_TO_DELIVER, CANCELED}

	@Id @GeneratedValue private long id;
	@Column(name = "user_id") private int user;
	private Status status;
	@Column(name = "created_at") private LocalDateTime createdAt;
	
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
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
				.add(status.toString())
				.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(createdAt))
				.toString();
	}
	
}
