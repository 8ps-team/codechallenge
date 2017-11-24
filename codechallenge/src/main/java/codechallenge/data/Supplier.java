package codechallenge.data;

import java.io.IOException;
import java.util.List;

//Object to handle Supplier records
//See src/main/reources/TransactionMap
public class Supplier extends MyBatisObject {
	private Integer id;
	private String name;
	private String address;
	private String contact;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "Supplier [id=" + id + ", Name=" + name + "]";
	}

	public Supplier() {
		super();
	}

	public Supplier(Integer id, String name, String address, String contact) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.contact = contact;
	}

	//Gets all suppliers
	public static List<Supplier> GetSuppliers() {
		try {
			OpenSession();

			List<Supplier> suppliers = session.selectList("Supplier.getAll");

			CloseSession();

			return suppliers;
		} catch (IOException e) {
			return null;
		}
	}
	
	//Gets a specific supplier
	public static Supplier GetSupplierById(int id) {
		try {
			OpenSession();

			Supplier supplier = session.selectOne("Supplier.getById", id);

			CloseSession();

			return supplier;
		} catch (IOException e) {
			return null;
		}
	}
	
	//Saves supplier - either insert or update based on whether an id is provided
	public Supplier Save() {
		try {
			OpenSession();

			if (this.getId() != null && this.getId() > 0) {
				session.update("Supplier.update", this);
			} else {
				int newId = session.insert("Supplier.insert", this);

				this.setId(newId);
			}
			
			CloseSession();
			
			return this;
		} catch (IOException e) {
			return null;
		}
	}
	
	//Deletes supplier by id
	public static Integer DeleteById(int id) {
		try {
			OpenSession();

			int deleted = session.delete("Supplier.deleteById", id);

			CloseSession();

			return deleted;
		} catch (IOException e) {
			return null;
		}
	}
}
