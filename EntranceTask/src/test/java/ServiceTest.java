import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import domain.Product;
import service.Service;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

	@Mock
	private static EntityManager entityManager;
	
	@Mock
	private static EntityTransaction tx;
	
	//@InjectMocks
	private static Service service;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	private Product productA = new Product();

	@BeforeEach
	void setUp() throws Exception {
		service = Service.getService(entityManager);
		when(entityManager.getTransaction()).thenReturn(tx);

		productA.setCreatedAt(LocalDateTime.of(2021,9,30,0,0));
		productA.setName("Test Product");
		productA.setPrice(BigDecimal.valueOf(10000, 2));
		productA.setStatus(Product.Status.IN_STOCK);
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Captor private ArgumentCaptor<?> productCaptor;

	@Test
	void testCreateProductSuccess() {
		assertEquals(0L,productA.getId());
		
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) {
				Product product = invocation.getArgument(0);
				product.setId(5L);
				return null;
			}}).when(entityManager).persist(same(productA));
		
		service.createProduct(productA);

		InOrder inOrder = inOrder(entityManager,tx);
		inOrder.verify(entityManager).getTransaction();
		inOrder.verify(tx).begin();
		inOrder.verify(entityManager).persist(productCaptor.capture());
		inOrder.verify(tx).commit();
		inOrder.verify(tx,never()).rollback();
		
		Product result = (Product)productCaptor.getValue();
		assertSame(productA, result);
		assertEquals("Test Product", result.getName());
		assertEquals(BigDecimal.valueOf(10000, 2), result.getPrice());
		assertEquals(Product.Status.IN_STOCK, result.getStatus());
		assertEquals(LocalDateTime.of(2021,9,30,0,0), result.getCreatedAt());

		assertEquals(5L, result.getId());
	}

	@Test
	void testCreateProductFail() {
		doThrow(PersistenceException.class).when(entityManager).persist(any());
		
		assertThrows(PersistenceException.class, ()->{
			service.createProduct(productA);			
		}, "persist should raise exception");
		
		InOrder inOrder = inOrder(entityManager,tx);
		inOrder.verify(entityManager).getTransaction();
		inOrder.verify(tx).begin();
		inOrder.verify(entityManager).persist(same(productA));
		inOrder.verify(tx).rollback();
		inOrder.verify(tx,never()).commit();

		assertEquals("Test Product", productA.getName());
		assertEquals(BigDecimal.valueOf(10000, 2), productA.getPrice());
		assertEquals(Product.Status.IN_STOCK, productA.getStatus());
		assertEquals(LocalDateTime.of(2021,9,30,0,0), productA.getCreatedAt());
		assertEquals(0L, productA.getId());
	}

/*
	@Test
	void testCreateOrderOrderProductArray() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateOrderOrderIterableOfProduct() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateOrderEntryQuantities() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllProducts() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllOrders() {
		fail("Not yet implemented");
	}

	@Test
	void testGetProductById() {
		fail("Not yet implemented");
	}

	@Test
	void testGetProductByIds() {
		fail("Not yet implemented");
	}

	@Test
	void testGetOrderById() {
		fail("Not yet implemented");
	}

	@Test
	void testGetOrderedProductsTotalQuantityDescending() {
		fail("Not yet implemented");
	}

	@Test
	void testGetOrderEntriesBy() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAllOrderEntries() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveProduct() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveAllProducts() {
		fail("Not yet implemented");
	}
*/
}
