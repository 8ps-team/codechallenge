import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import codechallenge.data.Transaction;

//Transaction integration tests:
//- get all transactions
//- search transaction with data that will definitely not match anything
//- search transaction by supplier name
//- search transaction by id
//- insert new transaction
public class TransactionIT extends CCTest {	
	@Test
	public void getAllTransactions() throws Exception {
		ResultSet rs = Conn.createStatement().executeQuery("select count(*) from `transaction`");
		rs.next();
		int databaseCount = rs.getInt(1);
		
		List<Transaction> allTransactions = Transaction.GetTransactions();
		
		int objectCount = allTransactions.size();
		
		assertEquals(databaseCount, objectCount);
	}
	
	@Test
	public void searchTransactionsNotFound() {
		String searchString = "%THISWILLDEFINITELYNOTBEFOUND%";
		
		assertEquals(Transaction.SearchTransactions(searchString).size(), 0);
	}
	
	@Test
	public void searchTransactionsFoundBySupplierName() throws Exception {
		ResultSet rs = Conn.createStatement().executeQuery("SELECT s.name FROM `supplier` s INNER JOIN `transaction` t GROUP BY s.id");
		
		List<Transaction> allTransactions = Transaction.GetTransactions();

		if (allTransactions.size() > 0 && rs.next()) {
			String supplierName = rs.getString(1);
			String searchString = "%" + supplierName + "%";
			
			assertTrue(Transaction.SearchTransactions(searchString).size() > 0);
		} else {
			assertTrue(true);
		}
	}
	
	@Test
	public void searchTransactionsFoundById() {
		List<Transaction> allTransactions = Transaction.GetTransactions();

		if (allTransactions.size() > 0) {
			Random rand = new Random();
			
			Transaction selectedTransaction = allTransactions.get(rand.nextInt(allTransactions.size() - 1)); 
			
			String uid = selectedTransaction.getId_text();
			String searchString = "%" + uid + "%";
			
			assertEquals(Transaction.SearchTransactions(searchString).get(0).getId_text(), selectedTransaction.getId_text());
		} else {
			assertTrue(true);
		}
	}
	
	@Test
	public void insertTransaction() throws Exception {
		ResultSet rs = Conn.createStatement().executeQuery("SELECT s.id FROM `supplier` s INNER JOIN `transaction` t GROUP BY s.id");
		if (rs.next()) {
			Transaction newTransaction = new Transaction();
			newTransaction.setSupplierId(rs.getInt(1));
			newTransaction.setContent("NEW TRANSACTION");
			
			newTransaction.Save();
			
			assertTrue(Transaction.SearchTransactions(newTransaction.getId_text()).size() == 1);
		} else {
			assertTrue(true);
		}
	}
}
