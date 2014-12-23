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
package org.activiti.engine.impl;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.AddCommentCmd;
import org.activiti.engine.impl.cmd.AddIdentityLinkCmd;
import org.activiti.engine.impl.cmd.ClaimTaskCmd;
import org.activiti.engine.impl.cmd.CompleteTaskCmd;
import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.cmd.DelegateTaskCmd;
import org.activiti.engine.impl.cmd.DeleteAttachmentCmd;
import org.activiti.engine.impl.cmd.DeleteCommentCmd;
import org.activiti.engine.impl.cmd.DeleteIdentityLinkCmd;
import org.activiti.engine.impl.cmd.DeleteTaskCmd;
import org.activiti.engine.impl.cmd.GetAttachmentCmd;
import org.activiti.engine.impl.cmd.GetAttachmentContentCmd;
import org.activiti.engine.impl.cmd.GetIdentityLinksForTaskCmd;
import org.activiti.engine.impl.cmd.GetProcessInstanceAttachmentsCmd;
import org.activiti.engine.impl.cmd.GetProcessInstanceCommentsCmd;
import org.activiti.engine.impl.cmd.GetSubTasksCmd;
import org.activiti.engine.impl.cmd.GetTaskAttachmentsCmd;
import org.activiti.engine.impl.cmd.GetTaskCommentsCmd;
import org.activiti.engine.impl.cmd.GetTaskEventsCmd;
import org.activiti.engine.impl.cmd.GetTaskVariableCmd;
import org.activiti.engine.impl.cmd.GetTaskVariablesCmd;
import org.activiti.engine.impl.cmd.RemoveTaskVariablesCmd;
import org.activiti.engine.impl.cmd.ResolveTaskCmd;
import org.activiti.engine.impl.cmd.SaveAttachmentCmd;
import org.activiti.engine.impl.cmd.SaveTaskCmd;
import org.activiti.engine.impl.cmd.SetTaskPriorityCmd;
import org.activiti.engine.impl.cmd.SetTaskVariablesCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.CopyHistoryTaskEntity;
import org.activiti.engine.impl.persistence.entity.CopyTaskEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ReadUserNames;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class TaskServiceImpl extends ServiceImpl implements TaskService {

  public Task newTask() {
    return newTask(null);
  }
  
  public Task newTask(String taskId) {
    TaskEntity task = TaskEntity.create();
    task.setId(taskId);
    return task;
  }
  
  public void saveTask(Task task) {
    commandExecutor.execute(new SaveTaskCmd(task));
  }
  
  public void deleteTask(String taskId) {
    commandExecutor.execute(new DeleteTaskCmd(taskId, null, false));
  }
  
  public void deleteTasks(Collection<String> taskIds) {
    commandExecutor.execute(new DeleteTaskCmd(taskIds, null, false));
  }
  
  public void deleteTask(String taskId, boolean cascade) {
    commandExecutor.execute(new DeleteTaskCmd(taskId, null, cascade));
  }

  public void deleteTasks(Collection<String> taskIds, boolean cascade) {
    commandExecutor.execute(new DeleteTaskCmd(taskIds, null, cascade));
  }
  
  @Override
  public void deleteTask(String taskId, String deleteReason) {
    commandExecutor.execute(new DeleteTaskCmd(taskId, deleteReason, false));
  }
  
  @Override
  public void deleteTasks(Collection<String> taskIds, String deleteReason) {
    commandExecutor.execute(new DeleteTaskCmd(taskIds, deleteReason, false));
  }

  public void setAssignee(String taskId, String userId) {
    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.ASSIGNEE));
  }
  
  public void setOwner(String taskId, String userId) {
    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.OWNER));
  }
  
  public void addCandidateUser(String taskId, String userId) {
    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.CANDIDATE));
  }
  
  public void addCandidateGroup(String taskId, String groupId) {
    commandExecutor.execute(new AddIdentityLinkCmd(taskId, null, groupId, IdentityLinkType.CANDIDATE));
  }
  
  public void addUserIdentityLink(String taskId, String userId, String identityLinkType) {
    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, identityLinkType));
  }

  public void addGroupIdentityLink(String taskId, String groupId, String identityLinkType) {
    commandExecutor.execute(new AddIdentityLinkCmd(taskId, null, groupId, identityLinkType));
  }
  
  public void deleteCandidateGroup(String taskId, String groupId) {
    commandExecutor.execute(new DeleteIdentityLinkCmd(taskId, null, groupId, IdentityLinkType.CANDIDATE));
  }

  public void deleteCandidateUser(String taskId, String userId) {
    commandExecutor.execute(new DeleteIdentityLinkCmd(taskId, userId, null, IdentityLinkType.CANDIDATE));
  }

  public void deleteGroupIdentityLink(String taskId, String groupId, String identityLinkType) {
    commandExecutor.execute(new DeleteIdentityLinkCmd(taskId, null, groupId, identityLinkType));
  }

  public void deleteUserIdentityLink(String taskId, String userId, String identityLinkType) {
    commandExecutor.execute(new DeleteIdentityLinkCmd(taskId, userId, null, identityLinkType));
  }
  
  public List<IdentityLink> getIdentityLinksForTask(String taskId) {
    return commandExecutor.execute(new GetIdentityLinksForTaskCmd(taskId));
  }
  
  public void claim(String taskId, String userId) {
    ClaimTaskCmd cmd = new ClaimTaskCmd(taskId, userId);
    commandExecutor.execute(cmd);
  }

  public void complete(String taskId) {
    commandExecutor.execute(new CompleteTaskCmd(taskId, null));
  }
  
//  public void complete( boolean returntoreject,String taskId) {
//	    commandExecutor.execute(new CompleteTaskCmd(taskId, returntoreject, (Map<String, Object>) null));
//	  }
  public void completeWithReason(String taskId,String completeReason,String bussinessop,String bussinessRemark)
  {
//	  commandExecutor.execute(new CompleteTaskCmd(taskId, null,completeReason));
	  commandExecutor.execute(new CompleteTaskCmd(taskId,completeReason, ( Map<String, Object>)null,(String)null, bussinessop, bussinessRemark));
  }
  
//  public void completeWithReason( boolean returntoreject,String taskId,String completeReason)
//  {
//	  commandExecutor.execute(new CompleteTaskCmd(taskId, completeReason, returntoreject,(Map<String, Object>)null));
//  }
  /**
   * Called when the task is successfully executed.
   * @param taskId the id of the task to complete, cannot be null.
   * @param destinationTaskKey the  destination taskKey of the task where trans to, if be null see method complete(String taskId).
   * @throws ActivitiObjectNotFoundException when no task exists with the given id.
   * @throws ActivitiException when this task is {@link DelegationState#PENDING} delegation.
   */
  public void completeWithDest(String taskId,String destinationTaskKey)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId, null,destinationTaskKey));
  }
  public void completeWithDestReason(String taskId,String destinationTaskKey,String completeReason,String bussinessop,String bussinessRemark)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId,completeReason, null,destinationTaskKey, bussinessop, bussinessRemark));
  }
  public void complete(String taskId, Map<String, Object> variables) {
    commandExecutor.execute(new CompleteTaskCmd(taskId, variables));
  }
  
//  public void complete( boolean returntoreject,String taskId, Map<String, Object> variables) {
//	    commandExecutor.execute(new CompleteTaskCmd(taskId, returntoreject, variables));
//	  }
  public void completeWithReason(String taskId, Map<String, Object> variables,String completeReason,String bussinessop,String bussinessRemark)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId,completeReason, variables,  bussinessop,  bussinessRemark));
  }
  
//  public void completeWithReason(boolean returntoreject,String taskId, Map<String, Object> variables,String completeReason)
//  {
//	  commandExecutor.execute(new CompleteTaskCmd(taskId,completeReason,returntoreject, variables));
//  }
  
  /**
   * Called when the task is successfully executed, 
   * and the required task parameters are given by the end-user.
   * 如果下一个任务是多实例任务，
   * 那么可以通过流程变量在运行式设置多实例任务执行的方式为串行还是并行
   * 变量的命名规范为：
   * taskkey.bpmn.behavior.multiInstance.mode
   * 取值范围为：
   * 	parallel
   * 	sequential
   * 说明：taskkey为对应的任务的定义id
   * 
   * 这个变量可以在设计流程时统一配置，可以启动流程实例时动态修改，也可以在上个活动任务完成时修改
   * 
   * 在任务完成时，可以通过变量destinationTaskKey动态指定流程跳转的目标地址
   * @param taskId the id of the task to complete, cannot be null.
   * @param variables task parameters. May be null or empty.
   * @param destinationTaskKey the  destination taskKey of the task where trans to, if be null see method complete(String taskId).
   * @throws ActivitiObjectNotFoundException when no task exists with the given id.
   */
  public void complete(String taskId, Map<String, Object> variables,String destinationTaskKey)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId, variables,destinationTaskKey));
  }
  public void completeWithReason(String taskId, Map<String, Object> variables,String destinationTaskKey,String reason,String bussinessop,String bussinessRemark)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId, reason,variables,destinationTaskKey,  bussinessop,  bussinessRemark));
  }
  public void complete(String taskId, Map<String, Object> variables,int op)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId, variables,op));
  }
  public void completeWithReason(String taskId, Map<String, Object> variables,int op,String reason,String bussinessop,String bussinessRemark)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId, variables,op,reason,  bussinessop,  bussinessRemark));
  }
  
  public void complete(String taskId, Map<String, Object> variables,int op,int rejectedtype)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId, variables,op, rejectedtype));
  }
  public void completeWithReason(String taskId, Map<String, Object> variables,int op,int rejectedtype,String reason,String bussinessop,String bussinessRemark)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId, variables,op, rejectedtype,reason,  bussinessop,  bussinessRemark));
  }
  
  public void completeWithReason(String taskId, Map<String, Object> variables,int op,String rejecttonode,String reason,String bussinessop,String bussinessRemark)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId,op,rejecttonode, variables,reason,false,  bussinessop,  bussinessRemark));
  }
  /**
   * 
   * @param taskId
   * @param variables
   * @param op 0 rejected 1 withdraw 2 jump
   * @param rejecttonode
   * @param reason
   * @param returntoreject
   * @param bussinessop
   * @param bussinessRemark
   */
  public void completeWithReason(String taskId, Map<String, Object> variables,int op,String rejecttonode,String reason,boolean returntoreject,String bussinessop,String bussinessRemark)
  {
	  commandExecutor.execute(new CompleteTaskCmd(taskId,op,rejecttonode, variables,reason,returntoreject,  bussinessop,  bussinessRemark));
  }
  public void delegateTask(String taskId, String userId) {
    commandExecutor.execute(new DelegateTaskCmd(taskId, userId));
  }

  public void resolveTask(String taskId) {
    commandExecutor.execute(new ResolveTaskCmd(taskId, null));
  }

  public void resolve(String taskId, Map<String, Object> variables) {
    commandExecutor.execute(new ResolveTaskCmd(taskId, variables));
  }

  public void setPriority(String taskId, int priority) {
    commandExecutor.execute(new SetTaskPriorityCmd(taskId, priority) );
  }
  
  public TaskQuery createTaskQuery() {
    return new TaskQueryImpl(commandExecutor);
  }
 
  public NativeTaskQuery createNativeTaskQuery() {
    return new NativeTaskQueryImpl(commandExecutor);
  }
  
  public Map<String, Object> getVariables(String executionId) {
    return commandExecutor.execute(new GetTaskVariablesCmd(executionId, null, false));
  }

  public Map<String, Object> getVariablesLocal(String executionId) {
    return commandExecutor.execute(new GetTaskVariablesCmd(executionId, null, true));
  }

  public Map<String, Object> getVariables(String executionId, Collection<String> variableNames) {
    return commandExecutor.execute(new GetTaskVariablesCmd(executionId, variableNames, false));
  }

  public Map<String, Object> getVariablesLocal(String executionId, Collection<String> variableNames) {
    return commandExecutor.execute(new GetTaskVariablesCmd(executionId, variableNames, true));
  }

  public Object getVariable(String executionId, String variableName) {
    return commandExecutor.execute(new GetTaskVariableCmd(executionId, variableName, false));
  }
  
  public Object getVariableLocal(String executionId, String variableName) {
    return commandExecutor.execute(new GetTaskVariableCmd(executionId, variableName, true));
  }
  
  public void setVariable(String executionId, String variableName, Object value) {
    if(variableName == null) {
      throw new ActivitiIllegalArgumentException("variableName is null");
    }
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(variableName, value);
    commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, false));
  }
  
  public void setVariableLocal(String executionId, String variableName, Object value) {
    if(variableName == null) {
      throw new ActivitiIllegalArgumentException("variableName is null");
    }
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(variableName, value);
    commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, true));
  }

  public void setVariables(String executionId, Map<String, ? extends Object> variables) {
    commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, false));
  }

  public void setVariablesLocal(String executionId, Map<String, ? extends Object> variables) {
    commandExecutor.execute(new SetTaskVariablesCmd(executionId, variables, true));
  }

  public void removeVariable(String taskId, String variableName) {
    Collection<String> variableNames = new ArrayList<String>();
    variableNames.add(variableName);
    commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, false));
  }

  public void removeVariableLocal(String taskId, String variableName) {
    Collection<String> variableNames = new ArrayList<String>(1);
    variableNames.add(variableName);
    commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, true));
  }

  public void removeVariables(String taskId, Collection<String> variableNames) {
    commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, false));
  }

  public void removeVariablesLocal(String taskId, Collection<String> variableNames) {
    commandExecutor.execute(new RemoveTaskVariablesCmd(taskId, variableNames, true));
  }

  public void addComment(String taskId, String processInstance, String message) {
    commandExecutor.execute(new AddCommentCmd(taskId, processInstance, message));
  }

  public List<Comment> getTaskComments(String taskId) {
    return commandExecutor.execute(new GetTaskCommentsCmd(taskId));
  }

  public List<Event> getTaskEvents(String taskId) {
    return commandExecutor.execute(new GetTaskEventsCmd(taskId));
  }

  public List<Comment> getProcessInstanceComments(String processInstanceId) {
    return commandExecutor.execute(new GetProcessInstanceCommentsCmd(processInstanceId));
  }

  public Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, InputStream content) {
    return commandExecutor.execute(new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, content, null));
  }

  public Attachment createAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, String url) {
    return commandExecutor.execute(new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, null, url));
  }

  public InputStream getAttachmentContent(String attachmentId) {
    return commandExecutor.execute(new GetAttachmentContentCmd(attachmentId));
  }

  public void deleteAttachment(String attachmentId) {
    commandExecutor.execute(new DeleteAttachmentCmd(attachmentId));
  }
  
  public void deleteComments(String taskId, String processInstanceId) {
    commandExecutor.execute(new DeleteCommentCmd(taskId, processInstanceId));
  }

  public Attachment getAttachment(String attachmentId) {
    return commandExecutor.execute(new GetAttachmentCmd(attachmentId));
  }

  public List<Attachment> getTaskAttachments(String taskId) {
    return commandExecutor.execute(new GetTaskAttachmentsCmd(taskId));
  }

  public List<Attachment> getProcessInstanceAttachments(String processInstanceId) {
    return commandExecutor.execute(new GetProcessInstanceAttachmentsCmd(processInstanceId));
  }

  public void saveAttachment(Attachment attachment) {
    commandExecutor.execute(new SaveAttachmentCmd(attachment));
  }

  public List<Task> getSubTasks(String parentTaskId) {
    return commandExecutor.execute(new GetSubTasksCmd(parentTaskId));
  }
//  private String rejecttoPretaskSQL = "select TASK_DEF_KEY_/**,END_TIME_*/ from ACT_HI_TASKINST a inner join  (select PROC_INST_ID_,id_ from ACT_HI_TASKINST where id_ = ?)  d on a.PROC_INST_ID_ = d.PROC_INST_ID_ where d.id_ <> a.ID_ and d.task_def_key_ <> a.task_def_key_ order by a.END_TIME_ desc";
  /**
   *
   * 将当前任务驳回到上一个任务处理人处，并更新流程变量参数
   * 如果需要改变处理人，可以通过指定变量的的方式设置
   * @param taskId
   * @param variables
   */
  public boolean rejecttoPreTask(String taskId, Map<String, Object> variables)
  {
	  TransactionManager tm = new TransactionManager();
	  try {
		  tm.begin();
		 
			this.complete(taskId, variables,TaskService.op_rejected);
			tm.commit();
			return true;
		}
	  	catch(ActivitiException w)
	  	{
	  		throw w;
	  	}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败：taskId="+taskId ,e);
		}
	  	finally
	  	{
	  		tm.release();
	  	}
  }
  public boolean rejecttoPreTask(String taskId, Map<String, Object> variables,String rejectReason,String bussinessop,String bussinessRemark)
  {
	  TransactionManager tm = new TransactionManager();
	  try {
		  tm.begin();
		 
			this.completeWithReason(taskId, variables,TaskService.op_rejected, rejectReason,  bussinessop,  bussinessRemark);
			tm.commit();
			return true;
		}
	  	catch(ActivitiException w)
	  	{
	  		throw w;
	  	}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败：taskId="+taskId ,e);
		}
	  	finally
	  	{
	  		tm.release();
	  	}
  }
  
  
  /**
   * 将当前任务驳回到上一个任务处理人处
   * @param taskId
   */
  public boolean rejecttoPreTask(String taskId)
  {
	  return rejecttoPreTask(taskId,(Map<String, Object>)null);
  }
  
  /**
   * 将当前任务驳回到上一个任务处理人处
   * @param taskId
   */
  public boolean rejecttoPreTask(String taskId,String rejectReason,String bussinessop,String bussinessRemark)
  {
	  return rejecttoPreTask(taskId,(Map<String, Object>)null, rejectReason,  bussinessop,  bussinessRemark);
  }
  
  
/***/
  /**
  *
  * 将当前任务驳回到上一个任务处理人处，并更新流程变量参数
  * 如果需要改变处理人，可以通过指定变量的的方式设置
  * rejectedtype 0-驳回上一个任务对应的节点 1-驳回到当前节点的上一个节点（多条路径暂时不支持）
  * @param taskId
  * @param variables
  */
 public boolean rejecttoPreTask(String taskId, Map<String, Object> variables,int rejectedtype )
 {
	  TransactionManager tm = new TransactionManager();
	  try {
		  tm.begin();
		 
			this.complete(taskId, variables,TaskService.op_rejected, rejectedtype);
			tm.commit();
			return true;
		}
	  	catch(ActivitiException w)
	  	{
	  		throw w;
	  	}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败：taskId="+taskId ,e);
		}
	  	finally
	  	{
	  		tm.release();
	  	}
 }
 /**
  * 
  * @param taskId
  * @param variables
  * @param rejectReason
  * @param rejectedtype 0-驳回上一个任务对应的节点 1-驳回到当前节点的上一个节点（多条路径暂时不支持）
  * @return
  */
 public boolean rejecttoPreTask(String taskId, Map<String, Object> variables,String rejectReason,int rejectedtype,String bussinessop,String bussinessRemark)
 {
	  TransactionManager tm = new TransactionManager();
	  try {
		    tm.begin();
		 
			this.completeWithReason(taskId, variables,TaskService.op_rejected,rejectedtype, rejectReason,  bussinessop,  bussinessRemark);
			tm.commit();
			return true;
		}
	  	catch(ActivitiException w)
	  	{
	  		throw w;
	  	}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败：taskId="+taskId ,e);
		}
	  	finally
	  	{
	  		tm.release();
	  	}
 }
 
 
 /**
  * 将当前任务驳回到上一个任务处理人处
  * @param taskId
  * @param 0-驳回上一个任务对应的节点 1-驳回到当前节点的上一个节点（多条路径暂时不支持）
  */
 public boolean rejecttoPreTask(String taskId,int rejectedtype)
 {
	  return rejecttoPreTask(taskId,(Map<String, Object>)null, rejectedtype);
 }
 
 /**
  * 将当前任务驳回到上一个任务处理人处
  * @param taskId
  */
 public boolean rejecttoPreTask(String taskId,String rejectReason,int rejectedtype,String bussinessop,String bussinessRemark)
 {
	  return rejecttoPreTask(taskId,(Map<String, Object>)null, rejectReason, rejectedtype,  bussinessop,  bussinessRemark);
 }
 
 /**
 *
 * 将当前任务驳回到上一个任务处理人处，并更新流程变量参数
 * 如果需要改变处理人，可以通过指定变量的的方式设置
 * rejectedtype 0-驳回上一个任务对应的节点 1-驳回到当前节点的上一个节点（多条路径暂时不支持）
 * @param taskId
 * @param variables
 */
public boolean rejecttoTask(String taskId, Map<String, Object> variables,String desttaskkey)
{
	return rejecttoTask( taskId,  variables,(String)null,desttaskkey, (String) null, (String) null);
}
/**
 * 
 * @param taskId
 * @param variables
 * @param rejectReason
 * @param rejectedtype 0-驳回上一个任务对应的节点 1-驳回到当前节点的上一个节点（多条路径暂时不支持）
 * @return
 */
public boolean rejecttoTask(String taskId, Map<String, Object> variables,String rejectReason,String desttaskkey,String bussinessop,String bussinessRemark)
{
	TransactionManager tm = new TransactionManager();
	  try {
		    tm.begin();
		 
			this.completeWithReason(taskId, variables,TaskService.op_rejected,desttaskkey, rejectReason,  bussinessop,  bussinessRemark);
			tm.commit();
			return true;
		}
	  	catch(ActivitiException w)
	  	{
	  		throw w;
	  	}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败：taskId="+taskId +"->"+desttaskkey ,e);
		}
	  	finally
	  	{
	  		tm.release();
	  	}
}


/**
 * 将当前任务驳回到上一个任务处理人处
 * @param taskId
 * @param 0-驳回上一个任务对应的节点 1-驳回到当前节点的上一个节点（多条路径暂时不支持）
 */
public boolean rejecttoTask(String taskId,String desttaskkey)
{
	return rejecttoTask( taskId,  (Map<String, Object>)null,(String)null,desttaskkey,(String )null,(String)null);
}

/**
 * 将当前任务驳回到上一个任务处理人处
 * @param taskId
 */
public boolean rejecttoTask(String taskId,String rejectReason,String desttaskkey,String bussinessop,String bussinessRemark)
{
	return rejecttoTask( taskId,  (Map<String, Object>)null,rejectReason,desttaskkey,  bussinessop,  bussinessRemark);
}
 /**
  * 获取当前任务的驳回节点 
  * @param taskId
  * @return 驳回节点数组，包含两个元素：第一个元素是上个任务环节对应的节点，第二个元素是当前节点的上一个节点
  */
 public String[] findRejectedNode(String taskId)
 {
	 String[] twonodes = new String[2] ;
	  	try {
	  		
	  		String pretaskKey = null;
	  		
	  		ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
	  		HashMap pid = executor.queryObject(HashMap.class,"getproc_def_id_bytaskid",taskId);
			ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.findProcessEngineConfigurationImpl().getRepositoryService())
					.getDeployedProcessDefinition((String)pid.get("PROC_DEF_ID_"));
			ActivityImpl act = def.findActivity((String)pid.get("TASK_DEF_KEY_"));
			boolean ismultiinst = act.isMultiTask();
			
			if(!ismultiinst)
			{
				pretaskKey = executor.queryObject(String.class,"rejecttoPretaskSQL", taskId);
				twonodes[0] = pretaskKey;

			}
			else
			{
				pretaskKey = executor.queryObject(String.class,"multirejecttoPretaskSQL", taskId);
				twonodes[0] = pretaskKey;

			}
  		

  		
			do
				{
					List<ActivityImpl> acts = act.getInActivities();
		  			
		  			if(acts != null && acts.size() > 0)
		  			{
	  					ActivityImpl pretask = acts.get(0);
	  	  				String type =  (String)pretask.getProperty("type");
	  	  				if(type.equals("userTask"))
	  	  				{
	  	  					twonodes[1] = pretask.getId();
	  	  					break;
	  	  				}
	  	  				act = pretask;
		  			}
		  			else
		  			{
		  				break;
		  			}
				}while(true);

	  		return twonodes;
	  			
		} catch (ActivitiException e) {
			throw e;
		}
	  	catch (Exception e) {
			throw new ActivitiException("获取驳回任务失败：["+taskId+"]",e);
		}
 }
 
 /**
  * 获取当前任务的驳回节点 
  * @param taskId
  * @return 驳回节点数组，包含两个元素：第一个元素是上个任务环节对应的节点，第二个元素是当前节点的上一个节点
  */
 public ActivityImpl[] findRejectedActivityNode(String taskId)
 {
	 String[] twonodes = new String[2] ;
	 ActivityImpl[] nodes = new ActivityImpl[2];
	  	try {
	  		
	  		String pretaskKey = null;
	  		
	  		
	  		ConfigSQLExecutor executor = findProcessEngineConfigurationImpl().getExtendExecutor();
	  		HashMap pid = executor.queryObject(HashMap.class,"getproc_def_id_bytaskid",taskId);
			ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)this.findProcessEngineConfigurationImpl().getRepositoryService())
					.getDeployedProcessDefinition((String)pid.get("PROC_DEF_ID_"));
			ActivityImpl act = def.findActivity((String)pid.get("TASK_DEF_KEY_"));
			boolean ismultiinst = act.isMultiTask();
			if(!ismultiinst)
			{
				pretaskKey = executor.queryObject(String.class,"rejecttoPretaskSQL", taskId);
				twonodes[0] = pretaskKey;

			}
			else
			{
				pretaskKey = executor.queryObject(String.class,"multirejecttoPretaskSQL", taskId);
				twonodes[0] = pretaskKey;

			}
  		

  		
  			
  			{
  				
  				
  				do
  				{
  					List<ActivityImpl> acts = act.getInActivities();
  		  			
  		  			if(acts != null && acts.size() > 0)
  		  			{
	  					ActivityImpl pretask = acts.get(0);
	  	  				String type =  (String)pretask.getProperty("type");
	  	  				if(type.equals("userTask"))
	  	  				{
	  	  					nodes[1] = pretask;
	  	  					break;
	  	  				}
	  	  				act = pretask;
  		  			}
  		  			else
  		  			{
  		  				break;
  		  			}
  				}while(true);
  				
  			}
  			
  			List<ActivityImpl> activities = act.getProcessDefinition().getActivities();
  			for(ActivityImpl _act:activities)
  			{
  				if(twonodes[0] != null && _act.getId().equals(twonodes[0]))
  				{
  					nodes[0] = _act;
  				}
  				
  			}
	  		return nodes;
	  			
		} catch (ActivitiException e) {
			throw e;
		}
	  	catch (Exception e) {
			throw new ActivitiException("获取驳回任务失败：["+taskId+"]",e);
		}
 }

@Override
public ActivityImpl findFirstNodeByteTask(String taskId) {
	
	try {
		ConfigSQLExecutor executor = findProcessEngineConfigurationImpl().getExtendExecutor();
  		HashMap pid = executor.queryObject(HashMap.class,"getproc_def_id_bytaskid",taskId);
  		return findFirstNodeByDefID((String)pid.get("PROC_DEF_ID_")) ;
	}  catch (ActivitiException e) {
		throw e;
	}
  	catch (Exception e) {
		throw new ActivitiException("findFirstNodeByteTask失败：["+taskId+"]",e);
	}
}

@Override
public ActivityImpl findFirstNodeByDefID(String processdefid) {
	try {
  		
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.findProcessEngineConfigurationImpl().getRepositoryService())
				.getDeployedProcessDefinition(processdefid);
		List<ActivityImpl> activities = def.getActivities();
		if(activities == null || activities.size() == 0)
			return null;
		for(ActivityImpl act:activities)
		{
			String type =  (String)act.getProperty("type");
				if(type.equals("startEvent"))
				{
					if(act.getOutActivities() != null && act.getOutActivities().size() > 0)
						return act.getOutActivities().get(0);
					
				}
		}
		return null;
	}  catch (ActivitiException e) {
		throw e;
	}
  	catch (Exception e) {
		throw new ActivitiException("findFirstNodeByDefID失败：["+processdefid+"]",e);
	}
}

@Override
public ActivityImpl findFirstNodeByDefKey(String processdefKey) {
	ProcessDefinition def = this.findProcessEngineConfigurationImpl().getRepositoryService()
	.createProcessDefinitionQuery()
	.processDefinitionKey(processdefKey)
	.latestVersion().singleResult();
	return findFirstNodeByDefID(def.getId());
}






@Override
public boolean rejecttoTask(String taskId, Map<String, Object> variables,
		String desttaskkey, boolean returntoreject) {
	return rejecttoTask( taskId, variables,
			(String)null, desttaskkey, returntoreject,(String)null,(String)null);
}

@Override
public boolean rejecttoTask(String taskId, Map<String, Object> variables,
		String rejectReason, String desttaskkey, boolean returntoreject,String bussinessop,String bussinessRemark) {
	TransactionManager tm = new TransactionManager();
	  try {
		  tm.begin();
		 
		  completeWithReason( taskId,  variables,TaskService.op_rejected,desttaskkey,rejectReason, returntoreject,  bussinessop,  bussinessRemark);
			tm.commit();
			return true;
		}
	  	catch(ActivitiException w)
	  	{
	  		throw w;
	  	}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败：taskId="+taskId ,e);
		}
	  	finally
	  	{
	  		tm.release();
	  	}
}

@Override
public boolean rejecttoTask(String taskId, String desttaskkey,
		boolean returntoreject) {
	return rejecttoTask( taskId, (Map<String, Object>)null,
			(String)null, desttaskkey, returntoreject,(String )null,(String)null);
}

@Override
public boolean rejecttoTask(String taskId, String rejectReason,
		String desttaskkey, boolean returntoreject,String bussinessop,String bussinessRemark) {
	return rejecttoTask( taskId, (Map<String, Object>)null,
			rejectReason, desttaskkey, returntoreject,  bussinessop,  bussinessRemark);
}

/**
 * 撤销任务时调用的方法
 * @param taskId
 * @param variables
 * @param rejectReason
 * @param desttaskkey
 * @param returntoreject
 * @param bussinessop
 * @param bussinessRemark
 * @return
 */
public boolean withdrawTask(String taskId, Map<String, Object> variables,
		String withdrawReason, String desttaskkey,String bussinessop,String bussinessRemark) {
	TransactionManager tm = new TransactionManager();
	  try {
		  tm.begin();
		 
		  completeWithReason( taskId,  variables,TaskService.op_withdraw,desttaskkey,withdrawReason, bussinessop,  bussinessRemark);
			tm.commit();
			return true;
		}
	  	catch(ActivitiException w)
	  	{
	  		throw w;
	  	}
	  	catch (Exception e) {
			throw new ActivitiException("驳回任务失败：taskId="+taskId ,e);
		}
	  	finally
	  	{
	  		tm.release();
	  	}
}

	/**
	 * 创建抄送/通知任务
	 */
	public void createCopyTasks(ExecutionEntity execution)
	{
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			String users = execution.getTaskContext().getCopyUsers();
			String orgs = execution.getTaskContext().getCopyOrgs();
			String process_id = execution.getProcessInstanceId();
			String process_key = execution.getProcessDefinitionId();
			process_key = process_key.substring(0,process_key.indexOf(":"));
			String businesskey = execution.getProcessBusinessKey();
			Timestamp copytime = new Timestamp(new Date().getTime());
			String actid = execution.getCurrentActivityId();
			String actname = execution.getCurrentActivityName();
			String actinstid = Context.getCommandContext().getHistoryManager().findActivityInstance(execution).getId();
			if(!StringUtil.isEmpty(users))
			{
				String[] users_ = users.split("\\,");
				CopyTaskEntity copyTaskEntity = null;
				List<CopyTaskEntity> tasks = new ArrayList<CopyTaskEntity>();
				for(String user:users_)
				{
					copyTaskEntity = new CopyTaskEntity();
					copyTaskEntity.setAct_id(actid);
					copyTaskEntity.setAct_instid(actinstid);
					copyTaskEntity.setAct_name(actname);
					copyTaskEntity.setCopytime(copytime);
					copyTaskEntity.setBusinesskey(businesskey);
					copyTaskEntity.setProcess_key(process_key);
					copyTaskEntity.setProcess_id(process_id);
					copyTaskEntity.setCoper(user);
					copyTaskEntity.setTasktype( execution.getTaskContext().isNotify()?TaskContext.TYPE_NOTIFY:TaskContext.TYPE_COPY);
					copyTaskEntity.setCopertype(TaskContext.COPER_TYPE_USER);
					tasks.add(copyTaskEntity);
				}
				executor.insertBeans("insertcopy",tasks);
			}
			if(!StringUtil.isEmpty(orgs))
			{
				String[] orgs_ = orgs.split("\\,");
				CopyTaskEntity copyTaskEntity = null;
				List<CopyTaskEntity> tasks = new ArrayList<CopyTaskEntity>();
				for(String org:orgs_)
				{
					copyTaskEntity = new CopyTaskEntity();
					copyTaskEntity.setAct_id(actid);
					copyTaskEntity.setAct_instid(actinstid);
					copyTaskEntity.setAct_name(actname);
					copyTaskEntity.setCopytime(copytime);
					copyTaskEntity.setBusinesskey(businesskey);
					copyTaskEntity.setProcess_key(process_key);
					copyTaskEntity.setProcess_id(process_id);
					copyTaskEntity.setTasktype( execution.getTaskContext().isNotify()?TaskContext.TYPE_NOTIFY:TaskContext.TYPE_COPY);
					copyTaskEntity.setCoper(org);
					copyTaskEntity.setCopertype(TaskContext.COPER_TYPE_ORG);
					tasks.add(copyTaskEntity);
				}
				executor.insertBeans("insertcopy",tasks);
			}
			tm.commit();
		} catch (Exception e) {
			throw new ActivitiException("createCopyTasks failed:",e);
		}
		finally
		{
			tm.release();
		}
	}
	/**
	 * 完成抄送/通知任务
	 * @param copytaskid
	 */
	public void completeCopyTask(String copytaskid,String copyuser)
	{
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			ConfigSQLExecutor executor =this.findProcessEngineConfigurationImpl().getExtendExecutor();
			
			CopyTaskEntity copyTaskEntity = executor.queryObject(CopyTaskEntity.class, "selectcopy", copytaskid);
			if(copyTaskEntity == null)
			{
				tm.commit();
				throw new ActivitiException("completeCopyTask["+copytaskid+"] failed:copy task do not exist.");
			}
			int exit = executor.queryObject(int.class, "hasread", copyTaskEntity.getAct_instid(),  copyuser);
			if(exit > 0)
			{
				tm.commit();
				return;
			}
			CopyHistoryTaskEntity copyHistoryTaskEntity = new CopyHistoryTaskEntity();
			copyHistoryTaskEntity.setAct_id(copyTaskEntity.getAct_id());
			copyHistoryTaskEntity.setAct_instid(copyTaskEntity.getAct_instid());
			copyHistoryTaskEntity.setAct_name(copyTaskEntity.getAct_name());
			copyHistoryTaskEntity.setCopytime(copyTaskEntity.getCopytime());
			copyHistoryTaskEntity.setBusinesskey(copyTaskEntity.getBusinesskey());
			copyHistoryTaskEntity.setProcess_key(copyTaskEntity.getProcess_key());
			copyHistoryTaskEntity.setProcess_id(copyTaskEntity.getProcess_id());
			
			if(copyTaskEntity.getCopertype() == TaskContext.COPER_TYPE_ORG)
			{
				String coperCNName = null;
		    	try
		    	{
		    		coperCNName = this.findProcessEngineConfigurationImpl().getUserInfoMap().getUserName(copyuser);
		    		copyHistoryTaskEntity.setCoperCNName(coperCNName);
		    	}
		    	catch(Exception e)
		    	{
		//    		e.printStackTrace();
		    	};
				copyHistoryTaskEntity.setCoper(copyuser);
				
				copyHistoryTaskEntity.setCoperorg(copyTaskEntity.getCoper());
			}
			else
			{
				if(copyuser.equals(copyTaskEntity.getCoper()))
				{
					String coperCNName = null;
			    	try
			    	{
			    		coperCNName = this.findProcessEngineConfigurationImpl().getUserInfoMap().getUserName(copyuser);
			    		copyHistoryTaskEntity.setCoperCNName(coperCNName);
			    	}
			    	catch(Exception e)
			    	{
			//    		e.printStackTrace();
			    	};
					copyHistoryTaskEntity.setCoper(copyuser);
				}
				else
				{
					String coperCNName = null;
			    	try
			    	{
			    		coperCNName =this.findProcessEngineConfigurationImpl().getUserInfoMap().getUserName(copyuser);
			    		copyHistoryTaskEntity.setCoperCNName(coperCNName);
			    	}
			    	catch(Exception e)
			    	{
			//    		e.printStackTrace();
			    	};
					copyHistoryTaskEntity.setCoper(copyuser);
				}
			}
			copyHistoryTaskEntity.setReadtime(new Timestamp(new Date().getTime()));
			copyHistoryTaskEntity.setCopyid(copytaskid);		
			executor.insertBean("inserthicopy", copyHistoryTaskEntity);
			if(copyTaskEntity.getCopertype() == TaskContext.COPER_TYPE_USER)
			{
				if(copyuser.equals(copyTaskEntity.getCoper()))
					executor.delete("deletecopy", copytaskid);
			}
			tm.commit();
		} catch (ActivitiException e) {
			throw e;
		}catch (Exception e) {
			throw new ActivitiException("completeCopyTask["+copytaskid+"] failed:",e);
		}
		finally
		{
			tm.release();
		}
	}
	/**
	 * 获取用户的通知和抄送任务
	 * @param user 用户账号
	 * @param orgs 用户隶属的机构列表（父子机构）
	 * @return
	 */
	public List<CopyTaskEntity> getUserCopyTasks(String user,List<String> orgs,String process_key,String businesskey)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			Map params = new HashMap();
			params.put("user", user);
			params.put("orgs", orgs);
			params.put("process_key", process_key);
			params.put("businesskey", businesskey);
			return executor.queryListBean(CopyTaskEntity.class, "getUserCopyTasks", params);
		} catch (Exception e) {
			throw new ActivitiException("getUserCopyTasks["+user+"] failed:",e);
		}
		
		
	}
	
	/**
	 * 获取用户的通知和抄送任务
	 * @param user 用户账号
	 * @param orgs 用户隶属的机构列表（父子机构）
	 * @return
	 */
	public ListInfo getUserCopyTasks(String user,List<String> orgs,String process_key,String businesskey,long offeset,int pagesize)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			Map params = new HashMap();
			params.put("user", user);
			params.put("orgs", orgs);
			params.put("process_key", process_key);
			params.put("businesskey", businesskey);
			return executor.queryListInfoBean(CopyTaskEntity.class, "getUserCopyTasks",  offeset,  pagesize, params);
		} catch (Exception e) {
			throw new ActivitiException("getUserCopyTasks["+user+"] failed:",e);
		}
		
		
	}
	/**
	 * 获取用户的通知和抄送任务
	 * @param user 用户账号
	 * @param orgs 用户隶属的机构列表（父子机构）
	 * @return
	 */
	public List<CopyTaskEntity> getAdminCopyTasks(String process_key,String businesskey)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			Map params = new HashMap();
			params.put("process_key", process_key);
			params.put("businesskey", businesskey);
			return executor.queryListBean(CopyTaskEntity.class, "getAdminCopyTasks" , params);
		} catch (Exception e) {
			throw new ActivitiException("getAdminCopyTasks[process_key="+process_key+",businesskey="+businesskey+"] failed:",e);
		}
		
		
	}
	/**
	 * 获取用户的通知和抄送任务
	 * @param user 用户账号
	 * @param orgs 用户隶属的机构列表（父子机构）
	 * @return
	 */
	public ListInfo getAdminCopyTasks(String process_key,String businesskey,long offeset,int pagesize)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			Map params = new HashMap();
			params.put("process_key", process_key);
			params.put("businesskey", businesskey);
			return executor.queryListInfoBean(CopyTaskEntity.class, "getAdminCopyTasks",  offeset,  pagesize, params);
		} catch (Exception e) {
			throw new ActivitiException("getAdminCopyTasks[process_key="+process_key+",businesskey="+businesskey+"] failed:",e);
		}
		
		
	}
	
	/**
	 * 获取根据活动任务id获取任务的阅读记录
	 * @param actinstid 活动任务id
	 * @return
	 */
	public List<CopyHistoryTaskEntity> getCopyTaskReadUsers(String actinstid)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			return executor.queryList(CopyHistoryTaskEntity.class, "getCopyTaskReadUsers", actinstid);
		} catch (Exception e) {
			throw new ActivitiException("getCopyTaskReadUsers[actinstid="+actinstid +"] failed:",e);
		}
	}
	
	/**
	 * 获取根据活动任务id获取任务的阅读记录
	 * @param actinstid 活动任务id
	 * @return
	 */
	public ListInfo getCopyTaskReadUsers(String actinstid,long offeset,int pagesize)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			return executor.queryListInfo(CopyHistoryTaskEntity.class, "getCopyTaskReadUsers",  offeset,  pagesize, actinstid);
		} catch (Exception e) {
			throw new ActivitiException("getCopyTaskReadUsers[actinstid="+actinstid +"] failed:",e);
		}
	}


	/**
	 * 获取根据活动任务id获取任务的阅读记录中文名称
	 * @param actinstid 活动任务id
	 * @return
	 */
	public String getCopyTaskReadUserNames(String actinstid)
	{
		try {
			final StringBuffer users = new StringBuffer();
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			executor.queryByNullRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					if(users.length() == 0)
						users.append(origine.getString("COPERCNName"));
					else
						users.append(",").append(origine.getString("COPERCNName"));
					
				}
				
			}, "getCopyTaskReadUserNames", actinstid);
			return users.toString();
		} catch (Exception e) {
			throw new ActivitiException("getCopyTaskReadUsers[actinstid="+actinstid +"] failed:",e);
		}
	}
	
	/**
	 * 获取用户阅读记录
	 * @param actinstid 活动任务id
	 * @return
	 */
	public ListInfo getUserReaderCopyTasks(String user,String process_key,String businesskey,long offeset,int pagesize)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			Map params = new HashMap();
			params.put("process_key", process_key);
			params.put("businesskey", businesskey);
			params.put("user", user);
			params.put("isAdmin", false);
			return executor.queryListInfoBean(CopyHistoryTaskEntity.class, "getUserReaderCopyTasks",  offeset,  pagesize,params);
		} catch (Exception e) {
			throw new ActivitiException("Get User ["+user +"] Readed CopyTasks failed:",e);
		}
	}	
	
	/**
	 * 管理员查看所有用户阅读记录
	 * @param actinstid 活动任务id
	 * @return
	 */
	public ListInfo getAdminUserReaderCopyTasks(String process_key,String businesskey,long offeset,int pagesize)
	{
		try {
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			Map params = new HashMap();
			params.put("process_key", process_key);
			params.put("businesskey", businesskey);
			params.put("isAdmin", true);
			return executor.queryListInfoBean(CopyHistoryTaskEntity.class, "getUserReaderCopyTasks",  offeset,  pagesize,params);
		} catch (Exception e) {
			throw new ActivitiException("getAdminUserReaderCopyTasks  failed:",e);
		}
	}	
	
	/**
	 * 获取根据活动任务id获取任务的阅读记录中文名称,只返回前limit个用户
	 * @param actinstid 活动任务id
	 * @return
	 */
	public ReadUserNames getCopyTaskReadUserNames(String actinstid,int limit)
	{
		try {
			final StringBuffer users = new StringBuffer();
			  
			ConfigSQLExecutor executor = this.findProcessEngineConfigurationImpl().getExtendExecutor();
			ListInfo listInfo = executor.queryListInfoByNullRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					if(users.length() == 0)
						users.append(origine.getString("COPERCNName"));
					else
						users.append(",").append(origine.getString("COPERCNName"));
					
				}
				
			}, "getCopyTaskReadUserNames", 0,limit,actinstid);
			ReadUserNames readUserNames = new ReadUserNames();
			readUserNames.setReadUserNames(users.toString());
			readUserNames.setTotalsize(listInfo.getTotalSize());
			readUserNames.setHasmoreRecord(listInfo.getTotalSize() > limit);
			return readUserNames;
		} catch (Exception e) {
			throw new ActivitiException("getCopyTaskReadUsers[actinstid="+actinstid +",返回前"+limit+"条记录] failed:",e);
		}
	}
}
