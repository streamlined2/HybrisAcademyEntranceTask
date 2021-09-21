package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Runner {

	public static void main(String[] args) {
		Properties props = new Properties();
		try {
			props.load(ClassLoader.getSystemResourceAsStream("local.properties"));
			try(Connection conn = DriverManager.getConnection(props.getProperty("url"), props)){
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println(meta.getDriverName());
				System.out.println(meta.getDriverVersion());
				System.out.println(meta.getURL());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
