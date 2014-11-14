package org.activiti.engine.impl.persistence.entity;

import java.sql.Timestamp;

import com.frameworkset.orm.annotation.PrimaryKey;

public class CopyTaskEntity {
	@PrimaryKey
	private String id;
    private int copertype;
    private String coper;
    private String process_id;
    private String process_key;
    private String businesskey;
    private Timestamp copytime;
    private String actid;
    private String actname;
    private String actinstid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCopertype() {
		return copertype;
	}
	public void setCopertype(int copertype) {
		this.copertype = copertype;
	}
	public String getCoper() {
		return coper;
	}
	public void setCoper(String coper) {
		this.coper = coper;
	}
	public String getProcess_id() {
		return process_id;
	}
	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}
	public String getProcess_key() {
		return process_key;
	}
	public void setProcess_key(String process_key) {
		this.process_key = process_key;
	}
	public String getBusinesskey() {
		return businesskey;
	}
	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}
	public Timestamp getCopytime() {
		return copytime;
	}
	public void setCopytime(Timestamp copytime) {
		this.copytime = copytime;
	}
	public String getActid() {
		return actid;
	}
	public void setActid(String actid) {
		this.actid = actid;
	}
	public String getActname() {
		return actname;
	}
	public void setActname(String actname) {
		this.actname = actname;
	}
	public String getActinstid() {
		return actinstid;
	}
	public void setActinstid(String actinstid) {
		this.actinstid = actinstid;
	}

}
