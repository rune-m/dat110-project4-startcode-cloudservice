package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(getHerokuAssignedPort());
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description

		post("/accessdevice/log/", (req, res) -> {

			Gson gson = new Gson();

			AccessMessage message = new Gson().fromJson(req.body(), AccessMessage.class);

			int id = accesslog.add(message.getMessage());
			AccessEntry entry = new AccessEntry(id, message.getMessage());

			return gson.toJson(entry);

		});

		get("/accessdevice/log/", (req, res) -> {
			return accesslog.toJson();
		});

		get("/accessdevice/log/:id", (req, res) -> {

			Gson gson = new Gson();

			String id = req.params("id");

			AccessEntry entry = accesslog.log.get(Integer.valueOf(id));

			if (entry == null) {
				res.status(404);
				return gson.toJson("No entries with id " + id + " on the server.");
			}

			return new Gson().toJson(entry);

		});

		put("/accessdevice/code/", (req, res) -> {

			Gson gson = new Gson();

			AccessCode code = gson.fromJson(req.body(), AccessCode.class);
			accesscode.setAccesscode(code.getAccesscode());
			return gson.toJson(code);

		});

		get("/accessdevice/code/", (req, res) -> {
			return new Gson().toJson(accesscode);
		});

		delete("/accessdevice/log/", (req, res) -> {
			accesslog.clear();
			return accesslog.toJson();
		});


		
    }

	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 8080; //return default port if heroku-port isn't set (i.e. on localhost)
	}
    
}
