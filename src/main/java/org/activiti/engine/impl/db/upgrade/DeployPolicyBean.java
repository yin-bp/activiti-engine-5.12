package org.activiti.engine.impl.db.upgrade;

public class DeployPolicyBean {
	private String table;
	private String prodefcolumn;
	private UpgradeCallback upgradeCallback;
	private String updatesql;	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getProdefcolumn() {
		return prodefcolumn;
	}
	public void setProdefcolumn(String prodefcolumn) {
		this.prodefcolumn = prodefcolumn;
	}
	public UpgradeCallback getUpgradeCallback() {
		return upgradeCallback;
	}
	public void setUpgradeCallback(UpgradeCallback upgradeCallback) {
		this.upgradeCallback = upgradeCallback;
	}
	public String getUpdatesql() {
		return updatesql;
	}
	public void setUpdatesql(String updatesql) {
		this.updatesql = updatesql;
	}

}
