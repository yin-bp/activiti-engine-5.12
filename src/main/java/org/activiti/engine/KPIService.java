package org.activiti.engine;

import java.util.Collection;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * 工作流
 * @author yinbp
 *
 */
public interface KPIService {
	
	public KPI buildKPI(DelegateExecution execution,Collection<String> candiates);
	
}
