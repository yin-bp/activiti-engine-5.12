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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.BeansConfigurationHelper;
import org.activiti.engine.repository.Deployment;
import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.orm.transaction.TransactionManager;

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
	private List<DeployPolicyBean> deployPolicyBeans;
	private static Logger log = Logger.getLogger(InstanceUpgrade.class);
	/**
	 * 流程实例版本升级
	 * @param processKey 要升级的流程
	 * @param oldversions 旧的流程实例版本 号数组
	 * @param toversion 要升级到的流程版本号
	 * @throws Exception
	 */
	public void upgradeInstances(String processKey) throws Exception
	{
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			HashMap procdef = executor.queryObjectByRowHandler(new RowHandler<HashMap>(){
	
				@Override
				public void handleRow(HashMap arg0, Record arg1) throws Exception {
					arg0.put("ID_", arg1.getString("ID_"));
					arg0.put("KEY_", arg1.getString("KEY_"));
					arg0.put("VERSION_", arg1.getInt("VERSION_"));
					arg0.put("DEPLOYMENT_ID_", arg1.getString("DEPLOYMENT_ID_"));
				}
				
			}, HashMap.class, "queryLastVersionProcdefByKey", processKey);
			_upgradeProcessInstances(procdef);
			tm.commit();
		}
		finally
		{
			tm.release();
		}
	}
	
	private void _upgradeProcessInstances(HashMap procdef) throws Exception
	{
		String KEY_ = (String)procdef.get("KEY_");
		
		int ver = (Integer)procdef.get("VERSION_");
		String DEPLOYMENT_ID_ = (String)procdef.get("DEPLOYMENT_ID_");
		String ID_ = (String)procdef.get("ID_");
		executor.update("updateRunTasks", ID_,KEY_,ver );
		executor.update("updateExecutes", ID_,KEY_,ver );
		executor.update("updateJobs", ID_,KEY_,ver );
		executor.update("updateIdentitylinks", ID_,KEY_,ver );
		executor.update("updateTaskinsts", ID_,KEY_,ver );
		executor.update("updateProcinsts", ID_,KEY_,ver );
		executor.update("updateActinsts", ID_,KEY_,ver );
		String likekey = KEY_ + ":%";
		//更新业务表中记录的流程定义id的记录为最新版本
		
		for(int j =0; deployPolicyBeans != null && j < deployPolicyBeans.size();j ++)
		{
			DeployPolicyBean pro = deployPolicyBeans.get(j);
			if(pro.getUpdatesql() != null )
			{
				
				SQLExecutor.update(pro.getUpdatesql(), ID_,likekey);
			}
			if(pro.getUpgradeCallback() != null)
			{
				pro.getUpgradeCallback().update(procdef);
			}
		}
	}
	
	/**
	 * 升级对应部署包中对应的流程旧版本任务实例
	 * @param deployment
	 * @throws Exception 
	 */
	public void instanceUpgrade(Deployment deployment) throws Exception
	{
		String deploymentId = deployment.getId();
		List<HashMap> procdefs = executor.queryListByRowHandler(new RowHandler<HashMap>(){

			@Override
			public void handleRow(HashMap arg0, Record arg1) throws Exception {
				arg0.put("ID_", arg1.getString("ID_"));
				arg0.put("KEY_", arg1.getString("KEY_"));
				arg0.put("VERSION_", arg1.getInt("VERSION_"));
				arg0.put("DEPLOYMENT_ID_", arg1.getString("DEPLOYMENT_ID_"));
			}
			
		}, HashMap.class, "queryProcdefsByDeployment", deploymentId);
		
		for(int i = 0; procdefs != null && i < procdefs.size(); i ++)
		{
			HashMap procdef = procdefs.get(i);
			_upgradeProcessInstances(procdef);
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
		List<HashMap> procdefs = executor.queryListByRowHandler(new RowHandler<HashMap>(){

			@Override
			public void handleRow(HashMap arg0, Record arg1) throws Exception {
				arg0.put("ID_", arg1.getString("ID_"));
				arg0.put("KEY_", arg1.getString("KEY_"));
				arg0.put("VERSION_", arg1.getInt("VERSION_"));
				arg0.put("DEPLOYMENT_ID_", arg1.getString("DEPLOYMENT_ID_"));
			}
			
		},HashMap.class, "queryProcdefsByDeployment", deploymentId);
		for(int i = 0; procdefs != null && i < procdefs.size(); i ++)
		{
			HashMap procdef = procdefs.get(i);
			String KEY_ = (String)procdef.get("KEY_");
			int ver = (Integer)procdef.get("VERSION_");
			String DEPLOYMENT_ID_ = (String)procdef.get("DEPLOYMENT_ID_");
			String ID_ = (String)procdef.get("ID_");
			List<HashMap> procinsts = executor.queryList(HashMap.class, "queryProcinsts", KEY_,ver);
			for(int j = 0; procinsts != null && j < procinsts.size(); j ++)
			{
				HashMap procinst = procinsts.get(j);
				runtimeService.deleteProcessInstance((String)procinst.get("PROC_INST_ID_"), "流程部署"+deployment.getName()+"使用删除历史版本未完成任务策略.","部署删除","部署流程时选择删除旧版本实例");
			}
			
			for(int j =0; deployPolicyBeans != null && j < deployPolicyBeans.size();j ++)
			{
				DeployPolicyBean pro = deployPolicyBeans.get(j);				
				if(pro.getUpgradeCallback() != null)
				{
					pro.getUpgradeCallback().delete(procdef);
				}
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
	public void init() {
		BaseApplicationContext context = BeansConfigurationHelper.getConfigBeanFactory();
		deployPolicyBeans = new ArrayList<DeployPolicyBean>();
		ProList proList=null;
		try {
			proList = context.getListProperty("deployPolicy");
		} catch (Exception e1) {
			log.error("流程配置文件部署策略配置信息加载失败：",e1);
		}
		StringBuffer sql = new StringBuffer();
		for(int j =0; proList != null && j < proList.size();j ++)
		{
			DeployPolicyBean policyBean = new DeployPolicyBean();			
			Pro pro = (Pro)proList.get(j);
			String table = pro.getStringExtendAttribute("table");
			policyBean.setTable(table);
			String processdefcolumn = pro.getStringExtendAttribute("processdefcolumn");
			policyBean.setProdefcolumn(processdefcolumn);
			if(table != null && processdefcolumn != null)
			{
				
				sql.append("update ").append(table)
					.append(" set ").append(processdefcolumn).append("=?")
					.append(" where ").append(processdefcolumn).append(" like ?");
				policyBean.setUpdatesql(sql.toString());
				sql.setLength(0);
			}
			String callback = pro.getStringExtendAttribute("upgradecallback");
			if(callback != null)
			{				
				try {
					Class<UpgradeCallback> clazz = (Class<UpgradeCallback>) Class.forName(callback);				
					UpgradeCallback upgradeCallback = clazz.newInstance();
					policyBean.setUpgradeCallback(upgradeCallback);
				} catch (Exception e) {
					log.error("", e);
				}
			}
			deployPolicyBeans.add(policyBean);
		}
		
	}

}
