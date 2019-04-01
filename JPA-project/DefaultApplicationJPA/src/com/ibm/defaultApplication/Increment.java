package com.ibm.defaultApplication;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the INCREMENT database table.
 * 
 */
@Entity
@NamedQuery(name="Increment.findAll", query="SELECT i FROM Increment i")
public class Increment implements Serializable {
	private static final long serialVersionUID = 1L;

	private String primarykey;

	private int thevalue;

	public Increment() {
	}

	@Id
	public String getPrimarykey() {
		return this.primarykey;
	}

	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}

	public int getThevalue() {
		return this.thevalue;
	}

	public void setThevalue(int thevalue) {
		this.thevalue = thevalue;
	}

}