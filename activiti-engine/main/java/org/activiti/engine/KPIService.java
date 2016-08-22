package org.activiti.engine;

import java.util.Collection;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * 工作流
 * @author yinbp
 *
 */
public interface KPIService {
	
	public KPI buildKPI(DelegateExecution execution,Collection<String> candiates,Date taskCreateTime) throws ActivitiException;
	public ControlParam getControlParam(DelegateExecution currentexecution,String taskKey) throws ActivitiException;
	public ControlParam getControlParam(String processInstanceId,String activieKey) throws ActivitiException;
	/**
	 * 流程实例结束后，对实例相关的外围扩展信息实时归档操作
	 * @param currentexecution
	 * @param processInstanceID
	 * @throws ActivitiException
	 */
	public void archiveProcessRuntimedata(DelegateExecution currentexecution,String processInstanceID) throws ActivitiException;
}
