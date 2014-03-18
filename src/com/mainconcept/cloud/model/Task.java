package com.mainconcept.cloud.model;

import java.io.IOException;
import java.io.Serializable;

public class Task implements Serializable, Comparable{
	
	private static final long serialVersionUID = -1890127076182137168L;
	private int minDuration;
	private int maxDuration;
	private String name;
	private Priority priority;
	
	private transient Object taskMonitor = new Object();
	
	public Task(String name, int minDuration, int maxDuration, Priority priority) {
		this.name = name;
		this.minDuration = minDuration;
		this.maxDuration = maxDuration;
		this.priority = priority;
	}	
	
	public int getMinDuration() {
		return minDuration;
	}	
	
	public int getMaxDuration() {
		return maxDuration;
	}	
	
	public String getName() {
		return name;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public void perform(int expectSeconds) {
		synchronized (taskMonitor) {
			try {
				taskMonitor.wait(expectSeconds*1000);				
				if (Math.random()>0.5) {
					taskMonitor.wait((long) (expectSeconds*Math.random()*1000));
					throw new RuntimeException();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException();
			}
		}		
	}
	
	@Override
	public int compareTo(Object o) {
		if (o == null) {
			return 1;
		}
		if (!(o instanceof Task)) {
			return 1;
		}
		
		return getPriority().getIntPriority() 
				- ((Task) o).getPriority().getIntPriority();
	}
	
	private void readObject(java.io.ObjectInputStream in)
		    throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		taskMonitor = new Object();
	}	
}


