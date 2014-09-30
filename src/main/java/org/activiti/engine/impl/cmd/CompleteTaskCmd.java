/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.engine.impl.cmd;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.TaskContext;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.frameworkset.common.poolman.ConfigSQLExecutor;


/**
 * @author Joram Barrez
 */
public class CompleteTaskCmd extends NeedsActiveTaskCmd<Void> {
      
  private static final long serialVersionUID = 1L;
  protected Map<String, Object> variables;
  
  protected String completeReason;
  protected String bussinessop;
  protected String bussinessRemark;
  protected boolean returntoreject; 
  
 
  
  public CompleteTaskCmd(String taskId, Map<String, Object> variables) {
	    super(taskId);
	    this.variables = variables;
	  }
	  public CompleteTaskCmd(String taskId, String completeReason,Map<String, Object> variables,String bussinessop,String bussinessRemark) {
	    super(taskId);
	    this.variables = variables;
	    this.completeReason = completeReason;
	    this.bussinessop = bussinessop;
	    this.bussinessRemark = bussinessRemark;
	  }
  /**
   * 完成任务指定跳转目标节点
   * added by biaoping.yin
   * @param taskId
   * @param variables
   * @param destinationTaskKey
   */
  public CompleteTaskCmd(String taskId, Map<String, Object> variables,String destinationTaskKey) {
    super(taskId,destinationTaskKey);
    if(destinationTaskKey != null)
    {
    	this.op = TaskService.op_jump;
    }
    this.variables = variables;
    
  }
  
  public CompleteTaskCmd(String taskId, String completeReason, Map<String, Object> variables,String destinationTaskKey,String bussinessop,String bussinessRemark) {
	    super(taskId,destinationTaskKey);
	    if(destinationTaskKey != null)
	    {
	    	this.op = TaskService.op_jump;
	    }
	    this.variables = variables;
	    this.completeReason = completeReason;
	    this.bussinessop = bussinessop;
	    this.bussinessRemark = bussinessRemark;
	  }
  protected String findTaskSourceRejectedNode(CommandContext commandContext)
  {
	 
	  	try {
	  		String pretaskKey = null;
	  				  	
				
				ConfigSQLExecutor executor = Context.getProcessEngineConfiguration().getExtendExecutor();
				
				pretaskKey = executor.queryObject(String.class,"findTaskSourceRejectedNode", taskId,TaskService.op_returntorejected);
					  		
			return pretaskKey;
		} catch (Exception e) {
			throw new ActivitiException("",e);
		}
	  
  }
  
  protected String findRejectedNode(CommandContext commandContext, TaskEntity task)
  {
	 
	  	try {
	  		ActivityImpl act = task.getExecution().getActivity();
	  		String pretaskKey = null;
	  		if(this.rejectedtype == 0)
	  		{
					  	
				boolean ismultiinst = act.isMultiTask();
				ConfigSQLExecutor executor = Context.getProcessEngineConfiguration().getExtendExecutor();
				
				if(!ismultiinst)
				{
					pretaskKey = executor.queryObject(String.class,"rejecttoPretaskSQL", taskId);
					if(pretaskKey == null)
					{
						throw new ActivitiException("驳回任务失败："+task.getTaskDefinitionKey()+"["+taskId+"],没有找到驳回节点!");
					}
				}
				else
				{
					pretaskKey = executor.queryObject(String.class,"multirejecttoPretaskSQL", taskId);
					if(pretaskKey == null)
					{
						throw new ActivitiException("驳回任务失败："+task.getTaskDefinitionKey()+"["+taskId+"],没有找到驳回节点!");
					}
				}
	  		}
	  		else
	  		{
	  			List<ActivityImpl> acts = act.getInActivities();
	  			if(acts != null && acts.size() > 0)
	  			{
	  				pretaskKey = acts.get(0).getId();
	  			}
	  			if(pretaskKey == null)
				{
					throw new ActivitiException("驳回任务失败："+task.getTaskDefinitionKey()+"["+taskId+"],没有找到驳回节点!");
				}
	  		}
			return pretaskKey;
		} catch (ActivitiException e) {
			throw e;
		}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败："+task.getTaskDefinitionKey()+"["+taskId+"]",e);
		}
  }
  public CompleteTaskCmd(String taskId, Map<String, Object> variables,int op) {
	    super(taskId,op,0);
	    this.variables = variables;
	    
	  }
  public CompleteTaskCmd(String taskId, Map<String, Object> variables,int op,String reason,String bussinessop,String bussinessRemark) {
	    super(taskId,op,0);
	    this.variables = variables;
	    this.completeReason = reason;
	    this.bussinessop = bussinessop;
	    this.bussinessRemark = bussinessRemark;
	  }
  public CompleteTaskCmd(String taskId,int op,String destinationTaskKey, Map<String, Object> variables) {
	  super( taskId, op, destinationTaskKey);  
	  this.variables = variables;
	  
}
  
  public CompleteTaskCmd(String taskId,int op,String destinationTaskKey, Map<String, Object> variables,String reason,boolean returntoreject,String bussinessop,String bussinessRemark) {
	  super( taskId,   op, destinationTaskKey);  
	  this.variables = variables;
	  this.completeReason = reason;
	  this.bussinessop = bussinessop;
	    this.bussinessRemark = bussinessRemark;
	  this.returntoreject = returntoreject;
	  
}
  public CompleteTaskCmd(String taskId,int op,String destinationTaskKey) {
	  super( taskId,   op, destinationTaskKey);  
	  
}
  /**
   * 
   * @param taskId
   * @param variables
   * @param isrejected
   * @param rejecttype 0-驳回上一个任务对应的节点 1-驳回到当前节点的上一个节点（多条路径暂时不支持）
   */
  public CompleteTaskCmd(String taskId, Map<String, Object> variables,int op,int rejecttype) {
	    super(taskId,  op,rejecttype);
	    this.variables = variables;
	    
	  }
public CompleteTaskCmd(String taskId, Map<String, Object> variables,int op,int rejecttype,String reason,String bussinessop,String bussinessRemark) {
	    super(taskId,  op,rejecttype);
	    this.variables = variables;
	    this.completeReason = reason;
	    this.bussinessop = bussinessop;
	    this.bussinessRemark = bussinessRemark;
	  }
  protected Void execute(CommandContext commandContext, TaskEntity task) {
    if (variables!=null) {
      task.setExecutionVariables(variables);
    }
    //task.complete();
    TaskContext taskContext = new TaskContext();
    /**
     * modified by biaoping.yin
     */
    if(this.op == TaskService.op_rejected )
    {
    	if(destinationTaskKey == null)
    		this.destinationTaskKey = findRejectedNode( commandContext,  task);
    }
    else if(this.op == TaskService.op_withdraw)
    {
    	
    }
    else// if(this.returntoreject)
    {
    	if(destinationTaskKey == null)//查找任务是否有关联的驳回节点，如果有，则任务直接跳转到上次驳回节点
    	{
    		this.destinationTaskKey = findTaskSourceRejectedNode( commandContext);
    		if(destinationTaskKey != null)
    		{
    			taskContext.setFromreject(true);
    		}
    	}
    		
    }
    
    taskContext.setDestinationTaskKey(destinationTaskKey);
    
    if(this.op == TaskService.op_rejected || this.op == TaskService.op_withdraw || this.op == TaskService.op_jump)
    {
	    taskContext.setRejectedtaskid(this.taskId);
	    taskContext.setRejectednode(task.getTaskDefinitionKey());
	    taskContext.setIsrejected(this.op == TaskService.op_rejected);
	    taskContext.setIswithdraw(this.op == TaskService.op_withdraw);
	    taskContext.setIsjump(this.op == TaskService.op_jump);
	    taskContext.setOp(op);
	    taskContext.setRejecttype(this.rejectedtype);
	    taskContext.setReturntoreject(returntoreject);
    }
//    if(completeReason == null)
//    {
////	    if(this.destinationTaskKey == null || this.destinationTaskKey.equals(""))
////	    	task.complete();
////	    else
//	    	task.complete(taskContext,this.completeReason,this.bussinessop,this.bussinessRemark);
//    }
//    else
//    {
////    	if(this.destinationTaskKey == null || this.destinationTaskKey.equals(""))
////	    	task.complete(null,this.completeReason);
////	    else
//	    	task.complete(taskContext,this.completeReason,this.bussinessop,this.bussinessRemark);
//    }
    task.complete(taskContext,this.completeReason,this.bussinessop,this.bussinessRemark);
    return null;
  }
  
  @Override
  protected String getSuspendedTaskException() {
    return "Cannot complete a suspended task";
  }

}
