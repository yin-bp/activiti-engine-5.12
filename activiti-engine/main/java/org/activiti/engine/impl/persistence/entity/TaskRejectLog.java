package org.activiti.engine.impl.persistence.entity;

public class TaskRejectLog {
	private String NEWTASKID  ;
	private String   REJECTTASKID  ;
	private String   REJECTNODE    ;
	private int OPTYPE        ;
	private String PROCESS_ID;
	public String getNEWTASKID() {
		return NEWTASKID;
	}
	public void setNEWTASKID(String nEWTASKID) {
		NEWTASKID = nEWTASKID;
	}
	public String getREJECTTASKID() {
		return REJECTTASKID;
	}
	public void setREJECTTASKID(String rEJECTTASKID) {
		REJECTTASKID = rEJECTTASKID;
	}
	public String getREJECTNODE() {
		return REJECTNODE;
	}
	public void setREJECTNODE(String rEJECTNODE) {
		REJECTNODE = rEJECTNODE;
	}
	public int getOPTYPE() {
		return OPTYPE;
	}
	public void setOPTYPE(int oPTYPE) {
		OPTYPE = oPTYPE;
	}
	public String getPROCESS_ID() {
		return PROCESS_ID;
	}
	public void setPROCESS_ID(String pROCESS_ID) {
		PROCESS_ID = pROCESS_ID;
	}

}
