/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Priyanka
 */
public class DataMap {

	HashMap<String, Object> propertyMap = new HashMap();
	ArrayList<HashMap> data = new ArrayList();
	Iterator<HashMap> it = null;

	public Double getDouble(String name) {
		return (Double) propertyMap.get(name);
	}

	public Float getFloat(String name) {
		return (Float) propertyMap.get(name);
	}

	public Integer getInt(String name) {
		Object intObj = propertyMap.get(name);
		if (intObj != null)
			return (Integer) intObj;
		else
			return 0;
	}
	
	public List getData() {
			return data;
	}
	public Date getDate(String name) {
		Date d;
		if (propertyMap.get(name) instanceof Timestamp) {
			d = new Date(((Timestamp) propertyMap.get(name)).getTime());
		} else {
			d = (Date) propertyMap.get(name);
		}
		return d;
	}

	public String getString(String name) {
		return (String) propertyMap.get(name);
	}

	public void put(String name, Object value) {
		propertyMap.put(name, value);
	}

	public void add() {
		data.add(propertyMap);
		propertyMap = new HashMap();
	}

	public DataMap first() {
		if (data.size() > 0)
			propertyMap = data.get(0);
		return this;
	}

	public boolean next() {
		if (it == null) {
			it = data.iterator();
		}
		if (it.hasNext()) {
			propertyMap = it.next();
			return true;
		} else {
			return false;
		}
	}

}
