package codechallenge;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import codechallenge.data.Supplier;

//API supplier REST functions
@Path("Suppliers")
public class Suppliers {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Supplier> GetSuppliers() {
		return Supplier.GetSuppliers();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Supplier GetSupplier(@PathParam("id") Integer id) {
		return Supplier.GetSupplierById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Supplier SaveSupplier(Supplier supplier) {
		return supplier.Save();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer DeleteSupplier(@PathParam("id") Integer id) {
		return Supplier.DeleteById(id);
	}
}
