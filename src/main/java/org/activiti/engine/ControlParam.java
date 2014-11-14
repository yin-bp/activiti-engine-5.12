package org.activiti.engine;

public class ControlParam {
	private String ID;


	private int IS_VALID;// 节点是否有效 0否1是

	private int IS_AUTO;// 节点处理人没有设置，自动执行业务处理类，然后生成下一节点任务
	private String BUSSINESSCONTROLCLASS;// 业务控制类
	
	private int IS_AUTOAFTER;// 后续节点自动审批 0不是1是（前后节点处理人一致，自动过掉后面的节点）

	private int IS_COPY;// 可抄送 0 不能 1 能

	private int IS_MULTI;// 是否多实例 0 单实例 1 多实例

	private int IS_SEQUENTIAL;// 是否串行 0 串行 1 并行


	private String NODE_KEY;// 节点key
	private String NODE_NAME;// 节点名称
	private String PROCESS_KEY;// 流程key
	private String PROCESS_ID;// 流程实例id
	private String copyUsers;
	private String copyOrgs;
	private String copyersCNName;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public int getIS_VALID() {
		return IS_VALID;
	}
	public void setIS_VALID(int iS_VALID) {
		IS_VALID = iS_VALID;
	}
	public int getIS_AUTO() {
		return IS_AUTO;
	}
	public void setIS_AUTO(int iS_AUTO) {
		IS_AUTO = iS_AUTO;
	}
	public String getBUSSINESSCONTROLCLASS() {
		return BUSSINESSCONTROLCLASS;
	}
	public void setBUSSINESSCONTROLCLASS(String bUSSINESSCONTROLCLASS) {
		BUSSINESSCONTROLCLASS = bUSSINESSCONTROLCLASS;
	}
	public int getIS_AUTOAFTER() {
		return IS_AUTOAFTER;
	}
	public void setIS_AUTOAFTER(int iS_AUTOAFTER) {
		IS_AUTOAFTER = iS_AUTOAFTER;
	}
	public int getIS_COPY() {
		return IS_COPY;
	}
	public void setIS_COPY(int iS_COPY) {
		IS_COPY = iS_COPY;
	}
	public int getIS_MULTI() {
		return IS_MULTI;
	}
	public void setIS_MULTI(int iS_MULTI) {
		IS_MULTI = iS_MULTI;
	}
	public int getIS_SEQUENTIAL() {
		return IS_SEQUENTIAL;
	}
	public void setIS_SEQUENTIAL(int iS_SEQUENTIAL) {
		IS_SEQUENTIAL = iS_SEQUENTIAL;
	}
	public String getNODE_KEY() {
		return NODE_KEY;
	}
	public void setNODE_KEY(String nODE_KEY) {
		NODE_KEY = nODE_KEY;
	}
	public String getNODE_NAME() {
		return NODE_NAME;
	}
	public void setNODE_NAME(String nODE_NAME) {
		NODE_NAME = nODE_NAME;
	}
	public String getPROCESS_KEY() {
		return PROCESS_KEY;
	}
	public void setPROCESS_KEY(String pROCESS_KEY) {
		PROCESS_KEY = pROCESS_KEY;
	}
	public String getPROCESS_ID() {
		return PROCESS_ID;
	}
	public void setPROCESS_ID(String pROCESS_ID) {
		PROCESS_ID = pROCESS_ID;
	}
	public String getCopyUsers() {
		return copyUsers;
	}
	public void setCopyUsers(String copyUsers) {
		this.copyUsers = copyUsers;
	}
	public String getCopyOrgs() {
		return copyOrgs;
	}
	public void setCopyOrgs(String copyOrgs) {
		this.copyOrgs = copyOrgs;
	}
	public String getCopyersCNName() {
		return copyersCNName;
	}
	public void setCopyersCNName(String copyersCNName) {
		this.copyersCNName = copyersCNName;
	}
}
