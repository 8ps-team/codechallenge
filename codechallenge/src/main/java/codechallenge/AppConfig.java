package codechallenge;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//Sets up basic API settings 
//Accessed @ http://host:port/codechallenge/v1/{resource}

@ApplicationPath("v1")
public class AppConfig extends Application {
	private Set<Class<?>> resources = new HashSet<>();
	
	public AppConfig() {
		resources.add(Suppliers.class);
		resources.add(Transactions.class);
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}
}
