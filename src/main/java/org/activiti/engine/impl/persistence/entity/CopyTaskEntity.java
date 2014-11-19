package org.activiti.engine.impl.persistence.entity;

import java.sql.Timestamp;

import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.PrimaryKey;

public class CopyTaskEntity {
	@PrimaryKey
	private String id;
    private int copertype;
    private String coper;
    private String coperCNName;
    private String process_id;
    private String process_key;
    private String businesskey;
    private Timestamp copytime;
   
    private String act_id;
    
    private String act_name;
    
    private String act_instid;
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
	
	public String getCoperCNName() {
		return coperCNName;
	}
	public void setCoperCNName(String coperCNName) {
		this.coperCNName = coperCNName;
	}
	public String getAct_id() {
		return act_id;
	}
	public void setAct_id(String act_id) {
		this.act_id = act_id;
	}
	public String getAct_name() {
		return act_name;
	}
	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}
	public String getAct_instid() {
		return act_instid;
	}
	public void setAct_instid(String act_instid) {
		this.act_instid = act_instid;
	}

}
