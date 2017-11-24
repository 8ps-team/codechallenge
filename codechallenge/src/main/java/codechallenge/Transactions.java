package codechallenge;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import codechallenge.data.Transaction;

//API transaction REST functions
@Path("Transactions")
public class Transactions {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Transaction> GetTransactions() {
		return Transaction.GetTransactions();
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Transaction> GetTransactions(String searchString) throws UnsupportedEncodingException {
		searchString = "%" + java.net.URLDecoder.decode(searchString.replace("searchString=",  ""), "UTF-8") + "%";
		
		return Transaction.SearchTransactions(searchString);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Transaction SaveTransaction(Transaction transaction) {
		return transaction.Save();
	}
}
