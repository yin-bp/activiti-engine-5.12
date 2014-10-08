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
	private ControlParam nextNodeControlParam;
	private boolean oneassignee = true;
	private boolean nextoneassignee = true;
	private boolean hasassignee = false;
	private TaskRejectLog taskRejectLog;
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
		if(oneassignee)
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

	public boolean isCOPY() {
		if(controlParam != null)
			return controlParam.getIS_COPY() != 0;
		else
			return false;
	}
	public ControlParam getControlParam() {
		return controlParam;
	}
	public void setControlParam(ControlParam controlParam) {
		this.controlParam = controlParam;
	}
	public ControlParam getNextNodeControlParam() {
		return nextNodeControlParam;
	}
	public void setNextNodeControlParam(ControlParam nextNodeControlParam) {
		this.nextNodeControlParam = nextNodeControlParam;
	}
	
	
	public boolean nextisIsmulti() {
		if(!BeansConfigurationHelper.getProcessEngineConfiguration().enableMixMultiUserTask())
			return false;
		if(nextoneassignee)
			return false;
		if(nextNodeControlParam != null)
			return nextNodeControlParam.getIS_MULTI() == 1;
		else
			return true;
	}
	
	public boolean nextisIsparrel() {
		if(nextNodeControlParam != null)
			return nextNodeControlParam.getIS_SEQUENTIAL() == 0;
		else
			return true;
	}
	
	
	public boolean nextisValidate() {
		if(nextNodeControlParam != null)
			return nextNodeControlParam.getIS_VALID() != 0;
		else
			return true;
	}
	
	public boolean nextisAuto() {
		if(nextNodeControlParam != null)
			return nextNodeControlParam.getIS_AUTO() != 0;
		else
			return true;
	}
	
	public String getNextBUSSINESSCONTROLCLASS() {
		if(nextNodeControlParam != null)
			return nextNodeControlParam.getBUSSINESSCONTROLCLASS();
		else
			return null;
	}
	
	public boolean nextisAUTOAFTER() {
		if(nextNodeControlParam != null)
			return nextNodeControlParam.getIS_AUTOAFTER() != 0;
		else
			return false;
	}

	public boolean nextisCOPY() {
		if(nextNodeControlParam != null)
			return nextNodeControlParam.getIS_COPY() != 0;
		else
			return false;
	}
	public boolean isOneassignee() {
		return oneassignee;
	}
	public void setOneassignee(boolean oneassignee) {
		this.oneassignee = oneassignee;
	}
	public boolean isNextoneassignee() {
		return nextoneassignee;
	}
	public void setNextoneassignee(boolean nextoneassignee) {
		this.nextoneassignee = nextoneassignee;
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
	
	
}
