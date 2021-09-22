package controller;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Runner {

	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			props.load(ClassLoader.getSystemResourceAsStream("local.properties"));
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("data", props);
			try {
				EntityManager em = null;
				try {
					em = factory.createEntityManager(props);
					System.out.println(em.isOpen());
				} finally {
					if(em!=null) em.close();
				}				
			}finally {
				factory.close();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
