package codechallenge.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

//basic class for mybatis objects
public class MyBatisObject {
	static String configres = "maps/mybatis-config.xml";
	static SqlSession session = null;
	
	public static void OpenSession() throws IOException {
		if (session != null) {
			session.close();
			session = null;
		}
		
		//Loads server info from config.properties and populates mybatis intializer
		Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        properties.load(loader.getResourceAsStream("config.properties"));
		
		InputStream reader = Resources.getResourceAsStream(configres);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
		session = sqlSessionFactory.openSession();
	}
	
	public static void CloseSession() {
		session.commit();
		session.close();
		
		session = null;
	}
}
