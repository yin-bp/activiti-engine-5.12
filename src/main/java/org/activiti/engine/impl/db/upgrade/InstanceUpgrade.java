/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.activiti.engine.impl.db.upgrade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;

import com.frameworkset.common.poolman.ConfigSQLExecutor;

/**
 * <p>Title: InstanceUpgrade.java</p>
 *
 * <p>Description: 流程实例和任务升级组件</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-6-13
 * @author biaoping.yin
 * @version 1.0
 */
public class InstanceUpgrade {
	private ConfigSQLExecutor executor;
	private TaskService taskService;
	private RuntimeService runtimeService;
	/**
	 * 升级对应部署包中对应的流程旧版本任务实例
	 * @param deployment
	 * @throws Exception 
	 */
	public void instanceUpgrade(Deployment deployment) throws Exception
	{
		String deploymentId = deployment.getId();
		List<HashMap> procdefs = executor.queryList(HashMap.class, "queryProcdefsByDeployment", deploymentId);
		for(int i = 0; procdefs != null && i < procdefs.size(); i ++)
		{
			HashMap procdef = procdefs.get(i);
			String KEY_ = (String)procdef.get("KEY_");
			Number VERSION_O = (Number)procdef.get("VERSION_");
			int ver = ((Number)VERSION_O).intValue();
			String DEPLOYMENT_ID_ = (String)procdef.get("DEPLOYMENT_ID_");
			String ID_ = (String)procdef.get("ID_");
			executor.update("updateRunTasks", ID_,KEY_,ver );
			executor.update("updateExecutes", ID_,KEY_,ver );
			executor.update("updateJobs", ID_,KEY_,ver );
			executor.update("updateIdentitylinks", ID_,KEY_,ver );
			executor.update("updateTaskinsts", ID_,KEY_,ver );
			executor.update("updateProcinsts", ID_,KEY_,ver );
			executor.update("updateActinsts", ID_,KEY_,ver );
		}
		
		/**
		 * act_ru_task
act_ru_job
act_ru_identitylink
act_ru_execution
act_hi_taskinst
act_hi_procinst
act_hi_actinst
		 */
	}
	/**
	 * 删除应部署包中对应的流程旧版本任务实例,删除其实采用的是
	 * @param deployment
	 */
	public void instanceDelete(Deployment deployment)  throws Exception
	{
		String deploymentId = deployment.getId();
		List<HashMap> procdefs = executor.queryList(HashMap.class, "queryProcdefsByDeployment", deploymentId);
		for(int i = 0; procdefs != null && i < procdefs.size(); i ++)
		{
			HashMap procdef = procdefs.get(i);
			String KEY_ = (String)procdef.get("KEY_");
			Number VERSION_O = (Number)procdef.get("VERSION_");
			int ver = ((Number)VERSION_O).intValue();
			String DEPLOYMENT_ID_ = (String)procdef.get("DEPLOYMENT_ID_");
			String ID_ = (String)procdef.get("ID_");
			List<HashMap> procinsts = executor.queryList(HashMap.class, "queryProcinsts", KEY_,ver);
			for(int j = 0; procinsts != null && j < procinsts.size(); j ++)
			{
				HashMap procinst = procinsts.get(j);
				runtimeService.deleteProcessInstance((String)procinst.get("PROC_INST_ID_"), "流程部署"+deployment.getName()+"使用删除历史版本未完成任务策略.");
			}
			
		}
	}
	public ConfigSQLExecutor getExecutor() {
		return executor;
	}
	public void setExecutor(ConfigSQLExecutor executor) {
		this.executor = executor;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
		
	}
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
		
	}

}
