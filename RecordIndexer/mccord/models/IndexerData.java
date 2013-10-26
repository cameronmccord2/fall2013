package models;

import java.util.ArrayList;

public class IndexerData {
	private ArrayList<Users> users;
	private ArrayList<Projects> projects;
	
	public ArrayList<Users> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<Users> users) {
		this.users = users;
	}
	public ArrayList<Projects> getProjects() {
		return projects;
	}
	public void setProjects(ArrayList<Projects> projects) {
		this.projects = projects;
	}
}
