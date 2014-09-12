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

package org.activiti.engine.impl.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ControlParam;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.TaskContext;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.BeansConfigurationHelper;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.jobexecutor.JobExecutorContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;
import org.apache.log4j.Logger;

import com.frameworkset.util.StringUtil;


/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class Context {

  protected static ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new ThreadLocal<Stack<CommandContext>>();
  protected static ThreadLocal<Stack<ProcessEngineConfigurationImpl>> processEngineConfigurationStackThreadLocal = new ThreadLocal<Stack<ProcessEngineConfigurationImpl>>();
  protected static ThreadLocal<Stack<ExecutionContext>> executionContextStackThreadLocal = new ThreadLocal<Stack<ExecutionContext>>();
  protected static ThreadLocal<JobExecutorContext> jobExecutorContextThreadLocal = new ThreadLocal<JobExecutorContext>();
  private static Logger log = Logger.getLogger(Context.class);
  private static Map<String,JavaDelegate> bussinessClassHandle = new HashMap<String,JavaDelegate>();
  
  public static JavaDelegate getJavaDelegate(String javaDelegateClass)
  {
	  if(StringUtil.isEmpty(javaDelegateClass))
	  {
		  throw new ActivitiException("Get JavaDelegate  failed:javaDelegateClass parameter is null.");
	  }
	  JavaDelegate temp =  bussinessClassHandle.get(javaDelegateClass);
	  if(temp != null)
		  return temp;
	  synchronized(bussinessClassHandle)
	  {
		  temp =  bussinessClassHandle.get(javaDelegateClass);
		  if(temp != null)
			  return temp;
		  
			
			int idx = javaDelegateClass.indexOf("{");
			if(idx > 0)
			{
				javaDelegateClass = javaDelegateClass.substring(idx+1,javaDelegateClass.length() -1);
				temp = BeansConfigurationHelper.getConfigBeanFactory().getTBeanObject(javaDelegateClass, JavaDelegate.class);
				if(temp == null)
					throw new ActivitiException("Get JavaDelegate for "+javaDelegateClass + " from BeanFactory Container ["+BeansConfigurationHelper.getConfigBeanFactory().getConfigfile()+ "] failed:"+javaDelegateClass + "未定义.");
				else
				{
					bussinessClassHandle.put(javaDelegateClass, temp);
					return temp;
				}
			}
			else
			{
				try {
					temp = (JavaDelegate)Class.forName(javaDelegateClass.trim()).newInstance();
					bussinessClassHandle.put(javaDelegateClass, temp);
					return temp;
					
				} catch (Exception e) {
					throw new ActivitiException("Get JavaDelegate of "+javaDelegateClass + " failed:",e);
				} 
			}
			
		  
	  }
  }
  public static boolean enableMixMultiUserTask()
  {
	  return BeansConfigurationHelper.getProcessEngineConfiguration().enableMixMultiUserTask();
  }
  public static CommandContext getCommandContext() {
    Stack<CommandContext> stack = getStack(commandContextThreadLocal);
    if (stack.isEmpty()) {
      return null;
    }
    return stack.peek();
  }

  public static void setCommandContext(CommandContext commandContext) {
    getStack(commandContextThreadLocal).push(commandContext);
  }

  public static void removeCommandContext() {
    getStack(commandContextThreadLocal).pop();
  }

  public static ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
    Stack<ProcessEngineConfigurationImpl> stack = getStack(processEngineConfigurationStackThreadLocal);
    if (stack.isEmpty()) {
      return null;
    }
    return stack.peek();
  }

  public static void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
    getStack(processEngineConfigurationStackThreadLocal).push(processEngineConfiguration);
  }

  public static void removeProcessEngineConfiguration() {
    getStack(processEngineConfigurationStackThreadLocal).pop();
  }

  public static ExecutionContext getExecutionContext() {
    return getStack(executionContextStackThreadLocal).peek();
  }

  public static void setExecutionContext(InterpretableExecution execution) {
    getStack(executionContextStackThreadLocal).push(new ExecutionContext(execution));
  }

  public static void removeExecutionContext() {
    getStack(executionContextStackThreadLocal).pop();
  }

  protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
    Stack<T> stack = threadLocal.get();
    if (stack==null) {
      stack = new Stack<T>();
      threadLocal.set(stack);
    }
    return stack;
  }
  
  public static JobExecutorContext getJobExecutorContext() {
    return jobExecutorContextThreadLocal.get();
  }
  
  public static void setJobExecutorContext(JobExecutorContext jobExecutorContext) {
    jobExecutorContextThreadLocal.set(jobExecutorContext);
  }
  
  public static void removeJobExecutorContext() {
    jobExecutorContextThreadLocal.remove();
  }
  private static void setTaskContextAssigneeInfo(ExecutionEntity execution,TaskContext taskContext)
  {
	  ActivityBehavior activityBehavior = execution.getActivity().getActivityBehavior();
		if(activityBehavior instanceof UserTaskActivityBehavior)
		{
			List<String>  users =((UserTaskActivityBehavior)activityBehavior).getAssignee(null, execution);
		
			if(StringUtil.isEmpty(users))
			{
				taskContext.setHasassignee(false);
				taskContext.setOneassignee(true);
			}
			else if(users.size() == 1)
			{
				if(!StringUtil.isEmpty(users.get(0)))
				{
					taskContext.setHasassignee(true);
					taskContext.setOneassignee(true);
				}
				else
				{
					taskContext.setHasassignee(false);
					taskContext.setOneassignee(true);
				}
			}
			else
			{
				taskContext.setHasassignee(true);
				taskContext.setOneassignee(false);
			}
		}
		else if(activityBehavior instanceof MultiInstanceActivityBehavior)
		{
			Collection users =((MultiInstanceActivityBehavior) activityBehavior).getAssignee(null, execution);
	 		
			if(StringUtil.isEmpty(users))
			{
				taskContext.setHasassignee(false);
				taskContext.setOneassignee(true);
			}
			else if(users.size() == 1)
			{
				if(!StringUtil.isEmpty(users.iterator().next()))
				{
					taskContext.setHasassignee(true);
					taskContext.setOneassignee(true);
				}
				else
				{
					taskContext.setHasassignee(false);
					taskContext.setOneassignee(true);
				}
			}
			else
			{
				taskContext.setHasassignee(true);
				taskContext.setOneassignee(false);
			} 
		}
		else
		{
			taskContext.setHasassignee(false);
			taskContext.setOneassignee(true);
		}
  }
  public static TaskContext createTaskContext(ExecutionEntity execution,String taskKey)
  {
	  TaskContext taskContext = new TaskContext();
	  try {
		  
	  		ControlParam controlParam = Context.getProcessEngineConfiguration().getKPIService().getControlParam(execution,taskKey);
			taskContext.setControlParam(controlParam);//设定当前任务的控制变量参数
			execution.setTaskContext(taskContext);
//			if(Context.enableMixMultiUserTask() )
			setTaskContextAssigneeInfo(execution,taskContext);
	  } catch (Exception e) {
			
			log.error("",e);
		}
			return taskContext;
			
  }
  
  public static void createTaskContextControlParam(TaskContext taskContext,ExecutionEntity execution,String taskKey)
  {
	  try {
	  		ControlParam controlParam = Context.getProcessEngineConfiguration().getKPIService().getControlParam(execution,taskKey);
			taskContext.setControlParam(controlParam);//设定当前任务的控制变量参数
			execution.setTaskContext(taskContext);
			setTaskContextAssigneeInfo(execution,taskContext);
	  } catch (Exception e) {
			
			log.error("",e);
		}
			
  }
}
