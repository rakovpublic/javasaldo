package com.rakov.saldo.model;

public class ServiceSupportModel {
	private int i;
	private int j;
	private String seg;

	public ServiceSupportModel(int i, int j, String seg) {
		super();
		this.i = i;
		this.j = j;
		this.seg = seg;
	}

	public ServiceSupportModel() {
		super();

	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public String getSeg() {
		return seg;
	}

	public void setSeg(String seg) {
		this.seg = seg;
	}
}
