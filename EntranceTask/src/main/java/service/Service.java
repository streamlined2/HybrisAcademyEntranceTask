package service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import domain.Order;
import domain.OrderEntry;
import domain.Product;

public class Service implements AutoCloseable {
	
	private static final String ORDER_ID = "id";
	private static final String PRODUCT_ID = "id";
	private static final String PRODUCT_NAME = "name";
	private static final String PRODUCT_PRICE = "price";
	private static final String PRODUCT_STATUS = "status";
	private static final String ORDER_ENTRY_QUANTITY = "quantity";
	private static final String PRODUCT_REF = "product";
	private static final String ORDER_REF = "order";
	private static final String ORDER_CREATED_AT = "createdAt";
	
	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	
	private Service() {
		try {
			Properties props = getProperties();
			entityManagerFactory = Persistence.createEntityManagerFactory("data", props);
			entityManager = entityManagerFactory.createEntityManager(props);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}					
	}
	
	public Properties getProperties() throws IOException {
		Properties props = new Properties();
		props.load(ClassLoader.getSystemResourceAsStream("local.properties"));
		return props;
	}
	
	private static class Holder {
		private static Service instance = new Service();
	}
	
	public static Service getService() { return Holder.instance;}
	public static EntityManager getEntityManager() { return Holder.instance.entityManager;}
	
	@Override
	public void close() {
		if(entityManager != null) entityManager.close();
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
	
	public Optional<Product> getProductById(long id){
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Product> query = cb.createQuery(Product.class);
		Root<Product> product = query.from(Product.class);
		query.select(product);
		query.where(cb.equal(product.get(PRODUCT_ID), Long.valueOf(id)));
		try {
			return Optional.of(getEntityManager().createQuery(query).getSingleResult());
		} catch(NoResultException e) {
			return Optional.empty();
		}
	}
	
	public List<Product> getProductByIds(Iterable<Long> ids){
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Product> query = cb.createQuery(Product.class);
		Root<Product> product = query.from(Product.class);
		query.select(product);
		query.where(cb.in(product.get(PRODUCT_ID)).in(Arrays.asList(ids)));//feature not implemented yet
		return getEntityManager().createQuery(query).getResultList();
	}
	
	public Optional<Order> getOrderById(long id){
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Order> query = cb.createQuery(Order.class);
		Root<Order> order = query.from(Order.class);
		query.select(order);
		query.where(cb.equal(order.get(ORDER_ID), Long.valueOf(id)));
		try {
			return Optional.of(getEntityManager().createQuery(query).getSingleResult());
		} catch(NoResultException e) {
			return Optional.empty();
		}
	}
	
	public List<Object[]> getOrderedProductsTotalQuantityDescending(){
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();
		Root<OrderEntry> orderEntry = query.from(OrderEntry.class);
		Join<OrderEntry,Product> product = orderEntry.join(PRODUCT_REF,JoinType.INNER);
		Expression<Long> sumEx = cb.sum(orderEntry.get(ORDER_ENTRY_QUANTITY));
		query.multiselect(List.of(
				product.get(PRODUCT_NAME),
				product.get(PRODUCT_STATUS),
				product.get(PRODUCT_PRICE),
				sumEx));
		query.groupBy(product.get(PRODUCT_NAME));
		query.orderBy(cb.desc(sumEx));
		return getEntityManager().createQuery(query).getResultList().stream().map(Tuple::toArray).toList();
	}
	
	public List<Object[]> getOrderEntriesBy(Order byOrder){
		var cb = getEntityManager().getCriteriaBuilder();
		var query = cb.createTupleQuery();
		var orderEntry = query.from(OrderEntry.class);
		var product = orderEntry.join(PRODUCT_REF,JoinType.INNER);
		var order = orderEntry.join(ORDER_REF,JoinType.INNER);
		Expression<BigDecimal> quantity = cb.toBigDecimal(orderEntry.get(ORDER_ENTRY_QUANTITY));
		Expression<BigDecimal> price = product.<BigDecimal>get(PRODUCT_PRICE);
		Expression<BigDecimal> totalPriceEx = cb.prod(quantity,price);
		query.multiselect(List.of(
					order.get(ORDER_ID),
					//totalPriceEx,
					product.get(PRODUCT_NAME),
					orderEntry.get(ORDER_ENTRY_QUANTITY),
					product.<BigDecimal>get(PRODUCT_PRICE),
					order.get(ORDER_CREATED_AT)
				));
		query.orderBy(cb.desc(order.get(ORDER_CREATED_AT)),cb.asc(product.get(PRODUCT_NAME)));
		query.where(cb.equal(order, byOrder));
		return getEntityManager().createQuery(query).getResultList().stream().map(Tuple::toArray).toList();
	}
	
	public List<Object[]> getAllOrderEntries(){
		var cb = getEntityManager().getCriteriaBuilder();
		var query = cb.createTupleQuery();
		var orderEntry = query.from(OrderEntry.class);
		var product = orderEntry.join(PRODUCT_REF,JoinType.INNER);
		var order = orderEntry.join(ORDER_REF,JoinType.INNER);
		Expression<BigDecimal> totalPriceEx = cb.prod(product.get(PRODUCT_PRICE),orderEntry.get(ORDER_ENTRY_QUANTITY));
		query.multiselect(List.of(
					order.get(ORDER_ID),
					//totalPriceEx,
					product.get(PRODUCT_NAME),
					orderEntry.get(ORDER_ENTRY_QUANTITY),
					order.get(ORDER_CREATED_AT)
				));
		query.orderBy(cb.desc(order.get(ORDER_CREATED_AT)),cb.asc(product.get(PRODUCT_NAME)));
		return getEntityManager().createQuery(query).getResultList().stream().map(Tuple::toArray).toList();
	}
	
	public Product removeProduct(Product product) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			getEntityManager().remove(product);
			tx.commit();
			return product;
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
