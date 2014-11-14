package org.activiti.engine.impl.persistence.entity;

import java.sql.Timestamp;

import com.frameworkset.orm.annotation.Column;
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
    @Column(name="act_id")
    private String actid;
    @Column(name="act_name")
    private String actname;
    @Column(name="act_instid")
    private String actinstid;
    private String coperCNName;
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
	public String getCoperCNName() {
		return coperCNName;
	}
	public void setCoperCNName(String coperCNName) {
		this.coperCNName = coperCNName;
	}
}
