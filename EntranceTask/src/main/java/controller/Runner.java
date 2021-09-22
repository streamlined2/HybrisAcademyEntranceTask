package controller;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Runner {
	
	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;		
	
	public static EntityManager getEntityManager() {
		if(entityManager == null) {
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
		return entityManager;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getEntityManager().isOpen());
		} finally {
			if(entityManagerFactory != null) entityManagerFactory.close();				
		}				
		
	}
	
}
