package org.activiti.engine.impl;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ControlParam;
import org.activiti.engine.KPI;
import org.activiti.engine.KPIService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.context.Context;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;

public class KPIServiceImpl implements KPIService {
	private KPIService KPIService;
	public KPIServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	public KPIServiceImpl(KPIService KPIService) {
		this.KPIService = KPIService;
	}

	@Override
	public KPI buildKPI(DelegateExecution execution,Collection<String> candiates,Date taskCreateTime) throws ActivitiException{
		if(KPIService != null)
			return KPIService.buildKPI(execution, candiates, taskCreateTime);
		else
			return null;
	}

	@Override
	public ControlParam getControlParam(DelegateExecution currentexecution,String taskKey) throws ActivitiException {
		if(KPIService != null)
			return KPIService.getControlParam(  currentexecution,  taskKey);
		else
			return null;
	}

	@Override
	public ControlParam getControlParam(String processInstanceId,
			String activieKey) throws ActivitiException {
		if(KPIService != null)
			return KPIService.getControlParam(  processInstanceId,
					  activieKey);
		else
			return null;
	}

	@Override
	public void archiveProcessRuntimedata(DelegateExecution currentexecution,
			String processInstanceID) throws ActivitiException {
		
		
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			Map condition = new HashMap();
			condition.put("processId", processInstanceID);
			
			condition.put("backuptime", new Timestamp(new Date().getTime()));
			
			 ConfigSQLExecutor executor = Context.getProcessEngineConfiguration().getExtendExecutor();
			  executor.insertBean("backuprejectlogToHi_wf", condition);
			  executor.delete("deleterejectlog", processInstanceID);

			tm.commit();
		} catch(ActivitiException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new ActivitiException("",e);
		}finally {
			tm.release();
		}
	
		if(KPIService != null)
			 KPIService.archiveProcessRuntimedata(  currentexecution,
					  processInstanceID);
			
		
	}
	
	

}
