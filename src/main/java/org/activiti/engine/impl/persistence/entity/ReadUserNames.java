package org.activiti.engine.impl.persistence.entity;

public class ReadUserNames {
	private String readUserNames;
	private long totalsize;
	private int pagesize;
	private boolean hasmoreRecord;
	public String getReadUserNames() {
		return readUserNames;
	}
	public void setReadUserNames(String readUserNames) {
		this.readUserNames = readUserNames;
	}
	public long getTotalsize() {
		return totalsize;
	}
	public void setTotalsize(long totalsize) {
		this.totalsize = totalsize;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public boolean isHasmoreRecord() {
		return hasmoreRecord;
	}
	public void setHasmoreRecord(boolean hasmoreRecord) {
		this.hasmoreRecord = hasmoreRecord;
	}

}
