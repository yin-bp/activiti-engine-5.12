package org.activiti.engine.impl;

import org.activiti.engine.ControlParam;
import org.activiti.engine.impl.cfg.BeansConfigurationHelper;
import org.activiti.engine.impl.persistence.entity.TaskRejectLog;

public class TaskContext {
	private String destinationTaskKey;
	private boolean isrejected;
	private boolean iswithdraw;
	private boolean isjump;
	private int op = -1;
	private String rejectednode;
	private String rejectedtaskid;
	private String newtaskid;
	private int rejecttype;
	private boolean returntoreject;
	private ControlParam controlParam;
	
	private boolean oneassignee = false;
	
	private boolean hasassignee = false;
	private TaskRejectLog taskRejectLog;
	
	public static final int TYPE_COPY = 1;
	public static final int TYPE_NOTIFY = 2;
	public static final int TYPE_TASK = 0;
	public static final int COPER_TYPE_USER = 0;
	public static final int COPER_TYPE_ORG = 1;
	public static final String CopyTaskBehavior = "org.activiti.engine.impl.bpmn.behavior.CopyTaskBehavior";
	/**
	 * 标识任务是否来自驳回
	 */
	private boolean fromreject = false;
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
	public boolean isIsmulti() {
		if(!BeansConfigurationHelper.getProcessEngineConfiguration().enableMixMultiUserTask())
			return false;
		if(oneassignee || !this.hasassignee)
			return false;
		if(controlParam != null)
			return controlParam.getIS_MULTI() == 1;
		else
			return false;
	}
	
	public boolean isIsparrel() {
		if(controlParam != null)
			return controlParam.getIS_SEQUENTIAL() == 0;
		else
			return true;
	}
	
	
	public boolean isValidate() {
		if(controlParam != null)
			return controlParam.getIS_VALID() != 0;
		else
			return true;
	}
	
	public boolean isAuto() {
		if(controlParam != null)
			return controlParam.getIS_AUTO() != 0;
		else
			return true;
	}
	
	public String getBUSSINESSCONTROLCLASS() {
		if(controlParam != null)
			return controlParam.getBUSSINESSCONTROLCLASS();
		else
			return null;
	}
	
	public boolean isAUTOAFTER() {
		if(controlParam != null)
			return controlParam.getIS_AUTOAFTER() != 0;
		else
			return false;
	}

	/**
	 * 抄送节点
	 * @return
	 */
	public boolean isCOPY() {
		if(controlParam != null)
			return controlParam.getIS_COPY() == TYPE_COPY;
		else
			return false;
	}
	/**
	 * 通知节点
	 * @return
	 */
	public boolean isNotify() {
		if(controlParam != null)
			return controlParam.getIS_COPY() == TYPE_NOTIFY;
		else
			return false;
	}
	public ControlParam getControlParam() {
		return controlParam;
	}
	public void setControlParam(ControlParam controlParam) {
		this.controlParam = controlParam;
	}
	
	public boolean isOneassignee() {
		return oneassignee;
	}
	public void setOneassignee(boolean oneassignee) {
		this.oneassignee = oneassignee;
	}
	
	public boolean isHasassignee() {
		return hasassignee;
	}
	public void setHasassignee(boolean hasassignee) {
		this.hasassignee = hasassignee;
	}
	public boolean isFromreject() {
		return fromreject;
	}
	public void setFromreject(boolean fromreject) {
		this.fromreject = fromreject;
	}
	public boolean isIswithdraw() {
		return iswithdraw;
	}
	public void setIswithdraw(boolean iswithdraw) {
		this.iswithdraw = iswithdraw;
	}
	public int getOp() {
		return op;
	}
	public void setOp(int op) {
		this.op = op;
	}
	public boolean isIsjump() {
		return isjump;
	}
	public void setIsjump(boolean isjump) {
		this.isjump = isjump;
	}
	public TaskRejectLog getTaskRejectLog() {
		return taskRejectLog;
	}
	public void setTaskRejectLog(TaskRejectLog taskRejectLog) {
		this.taskRejectLog = taskRejectLog;
	}
	public String getCopyUsers() {
		if(controlParam != null)
			return controlParam.getCopyUsers();
		else
			return null;
	}
	public String getCopyOrgs() {
		if(controlParam != null)
			return controlParam.getCopyOrgs();
		else
			return null;
	}
	public String getCopyersCNName() {
		if(controlParam != null)
			return controlParam.getCopyersCNName();
		else
			return null;
	}

	
	
}
