package com.gob.rewmobile.objects;

public class Mesa extends BaseClass {

	private int status;
	
	public Mesa(int id, String name) {
		super(id, name);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
