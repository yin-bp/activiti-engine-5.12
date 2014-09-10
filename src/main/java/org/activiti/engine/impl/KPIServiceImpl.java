package org.activiti.engine.impl;

import java.util.Collection;
import java.util.Date;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ControlParam;
import org.activiti.engine.KPI;
import org.activiti.engine.KPIService;
import org.activiti.engine.delegate.DelegateExecution;

public class KPIServiceImpl implements KPIService {

	public KPIServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public KPI buildKPI(DelegateExecution execution,Collection<String> candiates,Date taskCreateTime) throws ActivitiException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ControlParam getControlParam(DelegateExecution currentexecution,String taskKey) throws ActivitiException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ControlParam getControlParam(String processInstanceId,
			String activieKey) throws ActivitiException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
