package codechallenge.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

//Object to handle Transaction records
//See /src/main/resources/SupplierMap
public class Transaction extends MyBatisObject {
	private String id_text;
	private UUID id;
	private Integer supplierId;
	private Date currentDate;
	private String content;
	private String supplierName;

	public String getId_text() {
		return id_text;
	}

	public void setId_text(String id_text) {
		this.id_text = id_text;
		this.id = UUID.fromString(id_text);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", content=" + content + "]";
	}

	public Transaction(UUID id, Integer supplierId, Date currentDate, String content) {
		super();
		this.id = id;
		this.id_text = id.toString();
		this.supplierId = supplierId;
		this.currentDate = currentDate;
		this.content = content;
	}

	public Transaction() {
		super();
	}
	
	//Gets all transactions
	public static List<Transaction> GetTransactions() {
		try {
			OpenSession();

			List<Transaction> transactions = session.selectList("Transaction.getAll");

			CloseSession();

			return transactions;
		} catch (IOException e) {
			return null;
		}
	}
	
	//Searches a given string in transaction id, supplier id, supplier name, transaction content and transaction date
	public static List<Transaction> SearchTransactions(String searchString) {
		try {
			OpenSession();
			
			List<Transaction> transactions = session.selectList("Transaction.search", searchString);

			CloseSession();

			return transactions;
		} catch (IOException e) {
			return null;
		}
	}
	
	//Inserts given transaction
	public Transaction Save() {
		try {
			if (this.getCurrentDate() == null) this.setCurrentDate(new Date());
			if (this.getId() == null) { 
				this.setId(UUID.randomUUID()); 
				this.setId_text(this.id.toString());
			}

			OpenSession();

			session.insert("Transaction.insert", this);

			CloseSession();
			
			return this;
		} catch (IOException e) {
			return null;
		}
	}
}
