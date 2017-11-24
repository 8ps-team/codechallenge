package codechallengetransactions;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

public class Main {
	static Random rand = new Random();

	public static void main(String[] args) {
		System.out.println(" TRANSACTIONS GENERATOR ");
		System.out.println();

		String url = "http://localhost:9556";
		int transCount = 20;

		if (args.length > 0) {
			url = args[0];

			if (args.length > 1) {
				try {
					transCount = Integer.parseInt(args[1]);
				} catch (NumberFormatException ex) {
				}
			}
		}

		System.out.println("[>>] API URL set to: " + url);
		System.out.println("[>>] TRANSACTION-COUNT set to: " + transCount);
		System.out.println();
		
		System.out.print("[--] Getting suppliers: ");

		JSONArray suppliers;
		try {
			suppliers = Unirest.get(url + "/codechallenge/v1/Suppliers").asJson().getBody().getArray();

			if (suppliers.length() == 0) {
				System.out.println();
				System.out.print("[>>] No suppliers in system... adding dummy suppliers: ");
				
				for (int i = 1; i < 3; i++) {
					JSONObject obj = new JSONObject();
					obj.put("name", "Dummy Supplier " + i);
					obj.put("address", "Dummy Address");
					obj.put("contact", "");
	
					HttpRequestWithBody request = Unirest.post(url + "/codechallenge/v1/Suppliers");
					request.body(obj);
					request.header("Content-Type", "application/json");
	
					request.asString();
				}
				
				suppliers = Unirest.get(url + "/codechallenge/v1/Suppliers").asJson().getBody().getArray();
				if (suppliers.length() > 0) {
					System.out.println("OK");
				}
			} else {
				System.out.println(suppliers.length() + " suppliers found");
			}
			
			for (int i = 0; i < transCount; i++) {
				int supplierId = suppliers.getJSONObject(rand.nextInt(suppliers.length())).getInt("id");

				JSONObject obj = new JSONObject();
				obj.put("supplierId", supplierId);
				obj.put("content", getLipsumContent(rand.nextInt(10) + 3));

				HttpRequestWithBody request = Unirest.post(url + "/codechallenge/v1/Transactions");
				request.body(obj);
				request.header("Content-Type", "application/json");

				System.out.print("[" + ((i + 1) < 10 ? "0" + (i + 1) : (i + 1)) + "] Saving transaction: ");

				HttpResponse<String> response = request.asString();

				if (response == null || response.getBody() == null) {
					System.out.println("Failed");
				} else {
					System.out.println("OK");
				}
			}

		} catch (UnirestException e) {
			System.out.println("Failed");
			System.out.println("[!!] Connection with server @ " + url + " could not be established.");
		}

		System.out.println("[--] Done and exiting...");
	}

	private static String getLipsumContent(int contentLength) {
		String[] words = { "accumsan", "adipiscing", "aenean", "aliquam", "aliquet", "amet", "ante", "aptent", "arcu",
				"at", "auctor", "augue", "bibendum", "blandit", "class", "commodo", "condimentum", "congue",
				"consectetur", "consequat", "conubia", "convallis", "cras", "cubilia", "cum", "curabitur", "curae",
				"cursus", "dapibus", "diam", "dictum", "dictumst", "dignissim", "dis", "dolor", "donec", "dui", "duis",
				"egestas", "eget", "eleifend", "elementum", "elit", "enim", "erat", "eros", "est", "et", "etiam", "eu",
				"euismod", "facilisi", "facilisis", "fames", "faucibus", "felis", "fermentum", "feugiat", "fringilla",
				"fusce", "gravida", "habitant", "habitasse", "hac", "hendrerit", "himenaeos", "iaculis", "id",
				"imperdiet", "in", "inceptos", "integer", "interdum", "ipsum", "justo", "lacinia", "lacus", "laoreet",
				"lectus", "leo", "libero", "ligula", "litora", "lobortis", "lorem", "luctus", "maecenas", "magna",
				"magnis", "malesuada", "massa", "mattis", "mauris", "metus", "mi", "molestie", "mollis", "montes",
				"morbi", "mus", "nam", "nascetur", "natoque", "nec", "neque", "netus", "nibh", "nisi", "nisl", "non",
				"nostra", "nulla", "nullam", "nunc", "odio", "orci", "ornare", "parturient", "pellentesque",
				"penatibus", "per", "pharetra", "phasellus", "placerat", "platea", "porta", "porttitor", "posuere",
				"potenti", "praesent", "pretium", "primis", "proin", "pulvinar", "purus", "quam", "quis", "quisque",
				"rhoncus", "ridiculus", "risus", "rutrum", "sagittis", "sapien", "scelerisque", "sed", "sem", "semper",
				"senectus", "sit", "sociis", "sociosqu", "sodales", "sollicitudin", "suscipit", "suspendisse", "taciti",
				"tellus", "tempor", "tempus", "tincidunt", "torquent", "tortor", "tristique", "turpis", "ullamcorper",
				"ultrices", "ultricies", "urna", "ut", "varius", "vehicula", "vel", "velit", "venenatis", "vestibulum",
				"vitae", "vivamus", "viverra", "volutpat", "vulputate" };

		String output = "";
		for (int i = 0; i < contentLength; i++) {
			output += words[rand.nextInt(words.length)] + " ";
		}
		return output;
	}
}
