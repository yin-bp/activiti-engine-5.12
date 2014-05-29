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

import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
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
  
  
  
  public CompleteTaskCmd(String taskId, Map<String, Object> variables) {
    super(taskId);
    this.variables = variables;
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
    this.variables = variables;
    
  }
  protected String findRejectedNode(CommandContext commandContext, TaskEntity task)
  {
	 
	  	try {
			ActivityImpl act = task.getExecution().getActivity();		  	
			boolean ismultiinst = act.getActivityBehavior() instanceof MultiInstanceActivityBehavior;
			ConfigSQLExecutor executor = Context.getProcessEngineConfiguration().getExtendExecutor();
			String pretaskKey = null;
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
			return pretaskKey;
		} catch (ActivitiException e) {
			throw e;
		}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败："+task.getTaskDefinitionKey()+"["+taskId+"]",e);
		}
  }
  public CompleteTaskCmd(String taskId, Map<String, Object> variables,boolean isrejected) {
	    super(taskId,isrejected);
	    this.variables = variables;
	    
	  }
  
  protected Void execute(CommandContext commandContext, TaskEntity task) {
    if (variables!=null) {
      task.setExecutionVariables(variables);
    }
    //task.complete();
    /**
     * modified by biaoping.yin
     */
    if(this.isrejected && destinationTaskKey == null)
    {
    	this.destinationTaskKey = findRejectedNode( commandContext,  task);
    }
    if(this.destinationTaskKey == null || this.destinationTaskKey.equals(""))
    	task.complete();
    else
    	task.complete(this.destinationTaskKey);
    return null;
  }
  
  @Override
  protected String getSuspendedTaskException() {
    return "Cannot complete a suspended task";
  }

}
