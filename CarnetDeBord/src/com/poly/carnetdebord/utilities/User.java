package com.poly.carnetdebord.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonFactory;
import com.jayway.jsonpath.JsonPath;

/*
 * A class which represents the user of the application
 * 
 * 
 */
public class User {
	private long id;
	private Date creationDate;
	private String login;
	private String password;
	private String name;
	private String firstname;
	private Date birthdate;
	private boolean activate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setStringBirthdate(String birthdate) {
		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("Canada/Atlantic"));
		calendar.set(Integer.valueOf(birthdate.split("/")[0]),
				Integer.valueOf(birthdate.split("/")[1]),
				Integer.valueOf(birthdate.split("/")[2]));
		this.birthdate = calendar.getTime();
	}

	/*
	 * Method to know if the user has an activated account
	 * 
	 * @return true if account activated
	 */
	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	
	public void generateUser(String u)
	{
		this.login = JsonPath.read(u, "$.login");
		String date = JsonPath.read(u, "$.creationDate");
		try {

			this.creationDate = new SimpleDateFormat("EEEE MMM dd HH:mm:ss z yyyy").parse(date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer n = JsonPath.read(u, "$.userID");
		this.id = n.longValue();
		date = JsonPath.read(u, "$.birthDate");
        
		try {
			this.birthdate = new SimpleDateFormat("EEEE MMM dd HH:mm:ss z yyyy").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.firstname = JsonPath.read(u, "$.firstName");
		this.name = JsonPath.read(u, "$.name");
		this.password = JsonPath.read(u, "$.password");
		this.activate = true;
		
	}
}
