import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.util.List;

import org.junit.Test;

import codechallenge.data.Supplier;

//Supplier integration tests:
// - get all suppliers
// - get supplier by id with a bad id
// - get supplier by id with a good id
// - insert supplier
// - update supplier
// - delete supplier
public class SupplierIT extends CCTest {	
	@Test
	public void getAllSuppliers() throws Exception {
		ResultSet rs = Conn.createStatement().executeQuery("select count(*) from `supplier`");
		rs.next();
		int databaseCount = rs.getInt(1);
		
		List<Supplier> allSuppliers = Supplier.GetSuppliers();
		
		int objectCount = allSuppliers.size();
		
		assertEquals(databaseCount, objectCount);
	}
	
	@Test
	public void getSupplierByIdNotFound() {
		assertEquals(Supplier.GetSupplierById(-1), null);
	}
	
	@Test
	public void getSupplierByIdFound() throws Exception {
		ResultSet rs = Conn.createStatement().executeQuery("select id, name from `supplier` LIMIT 1");
		rs.next();
		int supplierId = rs.getInt(1);
		String supplierName = rs.getString(2);
		
		assertEquals(Supplier.GetSupplierById(supplierId).getName(), supplierName);
	}
	
	@Test
	public void insertSupplier() throws Exception {
		Supplier newSupplier = new Supplier();
		newSupplier.setAddress("NEW SUPPLIER ADDRESS");
		newSupplier.setContact("+1234567890");
		newSupplier.setName("NEWSUPPLIER");
		
		newSupplier.Save();
		
		ResultSet rs = Conn.createStatement().executeQuery("select name, address, contact from `supplier` ORDER BY id DESC LIMIT 1");
		rs.next();
		
		//all fields set
		if (rs.getString(1).equals(newSupplier.getName()) && rs.getString(2).equals(newSupplier.getAddress()) && rs.getString(3).equals(newSupplier.getContact())) {
			assertTrue(true);
		} else {
			assertTrue(false);
		}
	}
	
	@Test
	public void updateSupplier() throws Exception {
		ResultSet rs = Conn.createStatement().executeQuery("select id, name, address, contact from `supplier` LIMIT 1");
		if (rs.next()) {
			int supplierId = rs.getInt(1);
			String oldSupplierName = rs.getString(2);
			String oldAddress = rs.getString(3);
			String oldContact = rs.getString(4);
			
			Supplier supplier = new Supplier();
			supplier.setId(supplierId);
			supplier.setAddress("EDITED SUPPLIER ADDRESS");
			supplier.setContact("+EDITEDCONTACT");
			supplier.setName("EDITEDNAME");
			
			supplier.Save();
			
			rs = Conn.createStatement().executeQuery("select name, address, contact from `supplier` WHERE id = " + supplierId);
			rs.next();
			
			//all fields updated
			if (!(rs.getString(1).equals(oldSupplierName) || rs.getString(2).equals(oldAddress) || rs.getString(3).equals(oldContact))) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		} else assertTrue(true);
	}
	
	@Test
	public void deleteSupplier() throws Exception {
		ResultSet rs = Conn.createStatement().executeQuery("select id from `supplier` LIMIT 1");
		if (rs.next()) {
			int supplierId = rs.getInt(1);
			
			Supplier.DeleteById(supplierId);
			
			rs = Conn.createStatement().executeQuery("select id from `supplier` WHERE id = " + supplierId);
			assertTrue(!rs.next()); //test is successful if supplier is not found matching id
		} else assertTrue(true);
	}
}
