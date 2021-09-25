package service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;

import domain.Order;
import domain.OrderEntry;
import domain.Product;

public class Service implements AutoCloseable {
	
	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	
	private Service() {
		try {
			Properties props = new Properties();
			props.load(ClassLoader.getSystemResourceAsStream("local.properties"));
			entityManagerFactory = Persistence.createEntityManagerFactory("data", props);
			entityManager = entityManagerFactory.createEntityManager(props);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}					
	}
	
	private static class Holder {
		private static Service instance = new Service();
	}
	
	public static Service getService() { return Holder.instance;}
	public static EntityManager getEntityManager() { return Holder.instance.entityManager;}
	
	@Override
	public void close() {
		if(entityManagerFactory != null) entityManagerFactory.close();
	}
	
	public Product createProduct(Product product) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			getEntityManager().persist(product);
			tx.commit();			
			return product;
		} catch(Exception e) {
			tx.rollback();
			throw new PersistenceException(e);
		}
	}
	
	public Order createOrder(Order order, Product... products) {
		return createOrder(order, Arrays.asList(products));
	}
	
	public Order createOrder(Order order, Iterable<Product> products) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			getEntityManager().persist(order);
			for(Product product:products) {
				OrderEntry orderEntry = new OrderEntry();
				orderEntry.setProduct(product);
				orderEntry.setOrder(order);
				orderEntry.setQuantity(1);
				getEntityManager().persist(orderEntry);
			}
			tx.commit();			
			return order;
		} catch(Exception e) {
			tx.rollback();
			throw new PersistenceException(e);
		}
	}
	
	public void updateOrderEntryQuantities(Order order, Map<Product,Integer> quantities) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			for(var entry:quantities.entrySet()) {
				OrderEntry orderEntry = new OrderEntry();
				orderEntry.setOrder(order);
				orderEntry.setProduct(entry.getKey());
				orderEntry.setQuantity(entry.getValue());
				getEntityManager().merge(orderEntry);
			}
			getEntityManager().flush();
			tx.commit();			
		} catch(Exception e) {
			tx.rollback();
			throw new PersistenceException(e);
		}
	}
	
	public List<Product> getAllProducts(){
		var builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		query.from(Product.class);
		TypedQuery<Product> typedQuery = getEntityManager().createQuery(query);
		return typedQuery.getResultList();
	}
	
	public List<Order> getAllOrders(){
		var builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Order> query = builder.createQuery(Order.class);
		query.from(Order.class);
		TypedQuery<Order> typedQuery = getEntityManager().createQuery(query);
		return typedQuery.getResultList();
	}
	
	public Set<Product> getOrderedProductsTotalQuantityDescending(){
		return null;//TODO
	}
	
	public List<?> getOrderEntriesBy(Order order){
		return null;//TODO
	}
	
	public List<?> getAllOrderEntries(){
		return null;//TODO
	}
	
	public void removeProduct(Product product) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			getEntityManager().remove(product);
			tx.commit();			
		} catch(Exception e) {
			tx.rollback();
			throw new PersistenceException(e);
		}
	}
	
	public int removeAllProducts() {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaDelete<Product> criteriaDelete = cb.createCriteriaDelete(Product.class);
			criteriaDelete.from(Product.class);
			Query query = getEntityManager().createQuery(criteriaDelete);
			int cnt = query.executeUpdate();
			tx.commit();
			return cnt;
		} catch(Exception e) {
			tx.rollback();
			throw new PersistenceException(e);
		}
	}

}
