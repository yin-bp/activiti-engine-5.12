package org.activiti.engine.impl;

public class TaskContext {
	private String destinationTaskKey;
	private boolean isrejected;
	private String rejectednode;
	private String rejectedtaskid;
	private String newtaskid;
	private int rejecttype;
	private boolean returntoreject;
	public boolean isReturntoreject() {
		return returntoreject;
	}
	public void setReturntoreject(boolean returntoreject) {
		this.returntoreject = returntoreject;
	}
	public int getRejecttype() {
		return rejecttype;
	}
	public void setRejecttype(int rejecttype) {
		this.rejecttype = rejecttype;
	}
	public String getNewtaskid() {
		return newtaskid;
	}
	public void setNewtaskid(String newtaskid) {
		this.newtaskid = newtaskid;
	}
	public String getDestinationTaskKey() {
		return destinationTaskKey;
	}
	public void setDestinationTaskKey(String destinationTaskKey) {
		this.destinationTaskKey = destinationTaskKey;
	}
	public boolean isIsrejected() {
		return isrejected;
	}
	public void setIsrejected(boolean isrejected) {
		this.isrejected = isrejected;
	}
	public String getRejectednode() {
		return rejectednode;
	}
	public void setRejectednode(String rejectednode) {
		this.rejectednode = rejectednode;
	}
	public String getRejectedtaskid() {
		return rejectedtaskid;
	}
	public void setRejectedtaskid(String rejectedtaskid) {
		this.rejectedtaskid = rejectedtaskid;
	} 

}
