//
// 5630-A23, 5630-A22, (C) Copyright IBM Corporation, 2019
// All rights reserved. Licensed Materials Property of IBM
// Note to US Government users: Documentation related to restricted rights
// Use, duplication or disclosure is subject to restrictions set forth in GSA ADP Schedule with IBM Corp.
// This page may contain other proprietary notices and copyright information, the terms of which must be observed and followed.
//
//
package com.ibm.defaultapplication;

import javax.persistence.*;
import javax.ejb.*;
import com.ibm.defaultapplication.Increment;

@Stateless
public class IncrementSSB {
  @PersistenceContext(unitName="DefaultApplicationJPA")
  private EntityManager em;

  public int getTheValue() {
    Increment inc = em.find(Increment.class, "HitCount");
    return inc.getThevalue();
  }

  public int increment() {
	int value = 0;
    synchronized (this) {
        Increment inc = em.find(Increment.class, "HitCount");
        if (inc == null) {
        	inc = new Increment();
        	inc.setPrimarykey("HitCount");
        	inc.setThevalue(0);
        }
        value = inc.getThevalue();
        value++;
        inc.setThevalue(value);
        em.persist(inc);
    }
    return value;
  }
}
