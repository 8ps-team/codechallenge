import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/*
 *  Basic before/after setup class.
 *  
 *  Tests aimed to test same code used by REST API
 *  	by using a combination of JDBC mysql calls and matching with mybatis results.
 *  
 *  If the database is not found when running integration-test, it is created
 *  	and populated with dummy data.
 *  
 *  If the database is found but empty, it is populated with dummy data.
 *  
 *  If the database is found and it has content, then the tests are run on the
 *  	database content that already exists.
 *  
 *  After the tests are completed, the database is either emptied of dummy data
 *  	or returned to original state (if data already existed).
 */

public class CCTest {
	static boolean usingTestDB = false;
	static Connection Conn;
	static String databaseUser, databasePassword, databaseDomain, databasePort = null;
	static String databaseUrlBase, databaseUrlWithDB = null;

	@BeforeClass
	public static void setup() throws Exception {
		Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        properties.load(loader.getResourceAsStream("config.properties"));

        databaseDomain = properties.getProperty("domain");
        databasePort = properties.getProperty("port");
        databaseUser = properties.getProperty("user");
        databasePassword = properties.getProperty("pass");

        databaseUrlBase = "jdbc:mysql://" + databaseDomain + ":" + databasePort + "?user=" + databaseUser + "&password=" + databasePassword;
        databaseUrlWithDB = "jdbc:mysql://" + databaseDomain + ":" + databasePort + "/8pscodechallenge?user=" + databaseUser + "&password=" + databasePassword;
        
		Conn = DriverManager.getConnection(databaseUrlBase); 
		Statement statement = Conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '8pscodechallenge'");

        if (rs.next()) {
        	usingTestDB = false;
        	Conn = DriverManager.getConnection(databaseUrlWithDB);

        	CheckData();
        } else {
        	usingTestDB = true;
        	CreateEmptyDB();
        }
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		if (usingTestDB) { 
			Statement statement = Conn.createStatement();
	        statement.execute("DELETE FROM transaction");
	        statement.execute("DELETE FROM supplier");
	        
			System.out.println();
			System.out.println("> CLEANING 8PSCODECHALLENGE DATABASE FROM DEMO DATA");
			System.out.println();
		}
	}
	
	private static void CreateEmptyDB() throws Exception {
		System.out.println();
		System.out.println("> CREATING 8PSCODECHALLENGE DATABASE");
		
		Statement statement = Conn.createStatement();
		statement.execute("CREATE DATABASE 8pscodechallenge");
    	
    	Conn = DriverManager.getConnection(databaseUrlWithDB);
    	statement = Conn.createStatement();
    	
    	statement.execute("CREATE TABLE IF NOT EXISTS `transaction` (" + 
    			"  `id` char(36) NOT NULL," + 
    			"  `supplierid` int(11) NOT NULL," + 
    			"  `transactiondate` datetime NOT NULL," + 
    			"  `content` text NOT NULL" + 
    			") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
    	
    	statement.execute("CREATE TABLE IF NOT EXISTS `supplier` (" + 
    			"`id` int(4) NOT NULL," + 
    			"  `name` varchar(50) NOT NULL," + 
    			"  `address` varchar(255) NOT NULL," + 
    			"  `contact` varchar(15) DEFAULT NULL" + 
    			") ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;");
    	
    	statement.execute("ALTER TABLE `supplier` ADD PRIMARY KEY (`id`);");
    	statement.execute("ALTER TABLE `transaction` ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `id` (`id`);");
    	statement.execute("ALTER TABLE `supplier` MODIFY `id` int(4) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=20;");
    	
    	CheckData();
	}
	
	private static void CheckData() throws Exception {
		Statement statement = Conn.createStatement();
		
		ResultSet rs = Conn.createStatement().executeQuery("select count(*) from `transaction`");
		rs.next();
		int transactionCount = rs.getInt(1);
		
		rs = Conn.createStatement().executeQuery("select count(*) from `supplier`");
		rs.next();
		int supplierCount = rs.getInt(1);
		
		if (transactionCount == 0 && supplierCount == 0) {
			System.out.println();
			System.out.println("> POPULATING 8PSCODECHALLENGE DATABASE WITH DEMO DATA");

			usingTestDB = true;
			
	    	for (int i = 0; i < 5; i ++) {
	    		statement.execute("INSERT INTO `supplier` (name, address, contact) VALUES ('Supplier " + (i + 1) + "', 'Supplier address', '+1234567980')");
	    		int supplierId = 0;
	            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    supplierId = generatedKeys.getInt(1);
	                }
	            }
	            
				String transactionInsert = "INSERT INTO `transaction` (id, supplierid, transactiondate, content) VALUES ";
				
	    		for (int j = 0; j < 10; j++) {
	    			if (j > 0) transactionInsert += ", ";
	    			
	    			transactionInsert += "(UUID(), " + supplierId + ", NOW(), 'Sample content')";
	    		}
	    		
	    		statement.execute(transactionInsert);
	    	}
		}
	}
}
