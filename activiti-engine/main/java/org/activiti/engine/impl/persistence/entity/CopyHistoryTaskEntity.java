package org.activiti.engine.impl.persistence.entity;

import java.sql.Timestamp;

import com.frameworkset.orm.annotation.PrimaryKey;

public class CopyHistoryTaskEntity {
	@PrimaryKey
	private String id;
	private String copyid;
	private String coperorg;
    private String coper;
    private String process_id;
    private String process_key;
    private String businesskey;
    private Timestamp copytime;
    private Timestamp readtime;
    
    private String act_id;
    private String act_name;
    private String act_instid;
    private String coperCNName;
    /**
     * 0:抄送任务
     * 1：通知任务
     * 其他：扩展任务状态
     */
    private int tasktype;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCopyid() {
		return copyid;
	}
	public void setCopyid(String copyid) {
		this.copyid = copyid;
	}
	public String getCoperorg() {
		return coperorg;
	}
	public void setCoperorg(String coperorg) {
		this.coperorg = coperorg;
	}
	public Timestamp getReadtime() {
		return readtime;
	}
	public void setReadtime(Timestamp readtime) {
		this.readtime = readtime;
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
	public int getTasktype() {
		return tasktype;
	}
	public void setTasktype(int tasktype) {
		this.tasktype = tasktype;
	}
}
