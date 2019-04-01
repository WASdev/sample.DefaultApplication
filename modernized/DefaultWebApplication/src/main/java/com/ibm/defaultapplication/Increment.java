//
// 5630-A23, 5630-A22, (C) Copyright IBM Corporation, 2019
// All rights reserved. Licensed Materials Property of IBM
// Note to US Government users: Documentation related to restricted rights
// Use, duplication or disclosure is subject to restrictions set forth in GSA ADP Schedule with IBM Corp.
// This page may contain other proprietary notices and copyright information, the terms of which must be observed and followed.
//
//
package com.ibm.defaultapplication;

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