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

package org.activiti.engine.impl.pvm.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ControlParam;
import org.activiti.engine.impl.TaskContext;
import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MixMultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MixUserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmException;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

import com.frameworkset.util.StringUtil;


/**
 * @author Tom Baeyens
 */
public class ActivityImpl extends ScopeImpl implements PvmActivity, HasDIBounds {

  private static final long serialVersionUID = 1L;
  protected List<TransitionImpl> outgoingTransitions = new ArrayList<TransitionImpl>();
  protected Map<String, TransitionImpl> namedOutgoingTransitions = new HashMap<String, TransitionImpl>();
  protected List<TransitionImpl> incomingTransitions = new ArrayList<TransitionImpl>();
  protected ActivityBehavior activityBehavior;
  protected ScopeImpl parent;
  protected boolean isScope;
  protected boolean isAsync;
  protected boolean isExclusive;

  // Graphical information
  protected int x = -1;
  protected int y = -1;
  protected int width = -1;
  protected int height = -1;
  
  public ActivityImpl(String id, ProcessDefinitionImpl processDefinition) {
    super(id, processDefinition);
  }

  public TransitionImpl createOutgoingTransition() {
    return createOutgoingTransition(null);
  }
  /**
   * 返回任务是否是邮件任务
   * @return
   */
  public boolean isMailTask()
  {
	  if(activityBehavior != null )
	  {
		  boolean ret =  activityBehavior instanceof MailActivityBehavior;
		  if(ret )
			  return true;
		  if(activityBehavior instanceof MultiInstanceActivityBehavior )
		  {
			  return ((MultiInstanceActivityBehavior)activityBehavior).isMail();
		  }
	  }
	  return false;
  }
  /**
   * 返回任务类型是否是多实例任务类型
   * @return
   */
  public boolean isMultiTask()
  {
	  return activityBehavior != null && activityBehavior instanceof MultiInstanceActivityBehavior;
  }
  /**
   * 返回任务类型是否是并行多实例任务
   * @return
   */
  public boolean isParreal()
  {
	  return activityBehavior != null && 
			  (activityBehavior instanceof ParallelMultiInstanceBehavior 
					  || (activityBehavior instanceof MixMultiInstanceActivityBehavior && ((MixMultiInstanceActivityBehavior)activityBehavior).isParreal()));
	  
  }
  
  /**
   * 返回任务类型是否是串行多实例任务
   * @return
   */
  public boolean isSequence()
  {
	  return activityBehavior != null && 
			  (activityBehavior instanceof SequentialMultiInstanceBehavior 
					  || (activityBehavior instanceof MixMultiInstanceActivityBehavior && ((MixMultiInstanceActivityBehavior)activityBehavior).isSequence()));
	  
  }
  
  
  /**
   * 返回任务类型是否是多实例任务类型
   * @return
 * @throws ActivitiException 
   */
  public boolean isMultiTask(ActivityExecution execution,String procinstanceid,String taskid) throws ActivitiException
  {
	  try {
		boolean  isMultiTask = activityBehavior != null && activityBehavior instanceof MultiInstanceActivityBehavior;
		  if(isMultiTask)
			  return isMultiTask;
		  ControlParam controlParam = Context.getProcessEngineConfiguration().getKPIService().getControlParam(procinstanceid, getId());
		  if(controlParam == null)
			  return false;
		 
		  TaskContext taskContext = new TaskContext();
		  taskContext.setControlParam(controlParam);
		  if(execution != null && execution instanceof ExecutionEntity &&  execution.getActivity() != null && execution.getActivity().getId().equals(getId()))
		  {
			  Context.setTaskContextAssigneeInfo((ExecutionEntity) execution, taskContext);
		  }
		  else
		  {
			  String assignee = Context.getProcessEngineConfiguration().getExtendExecutor().queryObject(String.class, "getTaskAssignees", procinstanceid, getId() +"_users");
			  if(assignee == null || assignee.equals(""))
			  {
				  taskContext.setHasassignee(false);
				  taskContext.setOneassignee(false);
			  }
			  else
			  {
				  taskContext.setOneassignee(assignee.indexOf(",") < 0);
				  taskContext.setHasassignee(true);
			  }
		  }
		  return taskContext.isIsmulti();
	} catch (Exception e) {
		throw new ActivitiException("判断流程"+this.getProcessDefinition().getId()+"实例"+procinstanceid+"任务"+this.getId()+"节点是否是多实例任务失败：",e);
	}
  }
  /**
   * 返回任务类型是否是并行多实例任务
   * @return
   */
  public boolean isParreal(String procinstanceid,String taskid)
  {
	 
	  try {
		  
		  boolean isParreal = activityBehavior != null && 
				  (activityBehavior instanceof ParallelMultiInstanceBehavior 
						  || (activityBehavior instanceof MixMultiInstanceActivityBehavior && ((MixMultiInstanceActivityBehavior)activityBehavior).isParreal()));
			  if(isParreal)
				  return isParreal;
			  ControlParam controlParam = Context.getProcessEngineConfiguration().getKPIService().getControlParam(procinstanceid, getId());
			  if(controlParam == null)
				  return false;
			  String assignee = Context.getProcessEngineConfiguration().getExtendExecutor().queryObject(String.class, "getTaskAssignees", procinstanceid, getId());
			  TaskContext taskContext = new TaskContext();
			  taskContext.setControlParam(controlParam);
			  taskContext.setOneassignee(assignee != null && assignee.indexOf(",") < 0);
			  return taskContext.isIsmulti() && taskContext.isIsparrel();
		} catch (Exception e) {
			throw new ActivitiException("判断流程"+this.getProcessDefinition().getId()+"实例"+procinstanceid+"任务"+this.getId()+"节点是否是多实例并行任务失败：",e);
		}
	  
  }
  
  /**
   * 返回任务类型是否是串行多实例任务
   * @return
   */
  public boolean isSequence(String procinstanceid,String taskid)
  {
	  

	  try {
		  boolean isSequence = activityBehavior != null && 
				  (activityBehavior instanceof SequentialMultiInstanceBehavior 
						  || (activityBehavior instanceof MixMultiInstanceActivityBehavior && ((MixMultiInstanceActivityBehavior)activityBehavior).isSequence()));
			  if(isSequence)
				  return isSequence;
			  ControlParam controlParam = Context.getProcessEngineConfiguration().getKPIService().getControlParam(procinstanceid, getId());
			  if(controlParam == null)
				  return false;
			  String assignee = Context.getProcessEngineConfiguration().getExtendExecutor().queryObject(String.class, "getTaskAssignees", procinstanceid, getId());
			  TaskContext taskContext = new TaskContext();
			  taskContext.setControlParam(controlParam);
			  taskContext.setOneassignee(assignee != null && assignee.indexOf(",") < 0);
			  return taskContext.isIsmulti() && !taskContext.isIsparrel();
		} catch (Exception e) {
			throw new ActivitiException("判断流程"+this.getProcessDefinition().getId()+"实例"+procinstanceid+"任务"+this.getId()+"节点是否是多实例串行任务失败：",e);
		}
	  
	  
  }
  private Boolean isUserTask =null;
  public boolean isUserTask()
  {
	  if(isUserTask == null)
	  {
		  boolean usetask1 = this.activityBehavior != null && this.activityBehavior instanceof UserTaskActivityBehavior ;
		  boolean usetask2 = this.activityBehavior != null && this.activityBehavior instanceof MultiInstanceActivityBehavior && ((MultiInstanceActivityBehavior)this.activityBehavior ).isUserTask();
		  this.isUserTask = usetask1 || usetask2;
	  }
	  return this.isUserTask.booleanValue();
  }
  private List<ActivityImpl> inactivies ;
  
  private List<ActivityImpl> outactivies ;
  /**
   * 获取流程的入节点id清单
   * @return
   */
  public List<ActivityImpl> getInActivities()
  {
	  if(this.incomingTransitions == null || incomingTransitions.size() == 0)
		  return null;
	  if(inactivies != null)
		  return inactivies;
	  List<ActivityImpl> ret = new ArrayList<ActivityImpl>();
	  for(TransitionImpl trs:incomingTransitions)
	  {
		  ret.add(trs.getSource());
	  }
	  return inactivies = ret;
		  
  }
  
  /**
   * 获取流程的入节点id清单
   * @return
   */
  public List<ActivityImpl> getOutActivities()
  {
	  if(this.outgoingTransitions == null || outgoingTransitions.size() == 0)
		  return null;
	  if(outactivies != null)
		  return outactivies;
	  List<ActivityImpl> ret = new ArrayList<ActivityImpl>();
	  for(TransitionImpl trs:outgoingTransitions)
	  {
		  ret.add(trs.getDestination());
	  }
	  return outactivies = ret;
		  
  }
  /**
   * 创建流程临时迁移路径，在运行时为流程实例增加额外的执行路径，用来实现自由流功能
   * @param transitionId
   * @param destinationTaskKey
   * @return
   */
  public TransitionImpl createCustomOutgoingTransition(String transitionId, String destinationTaskKey ) {
	  TransitionImpl transition = new TransitionImpl(transitionId, processDefinition);
	    transition.setSource(this);
	    ActivityImpl destinationTask = processDefinition.findActivity(destinationTaskKey);
	    if(destinationTask == null)
	    	throw new PvmException("Create Custom OutgoingTransition for activity '"+id+"' with transitionId '"+transitionId+"' and destinationTaskKey '"+destinationTaskKey+"' failed: activity[" + destinationTaskKey +"] not found in process[" + processDefinition.getId() +"]");
	    transition.setCustomDestination(destinationTask);
//	    outgoingTransitions.add(transition);
//	    
//	    if (transitionId!=null) {
//	      if (namedOutgoingTransitions.containsKey(transitionId)) {
//	        throw new PvmException("activity '"+id+" has duplicate transition '"+transitionId+"'");
//	      }
//	      namedOutgoingTransitions.put(transitionId, transition);
//	    }
	    
	    return transition;
  }

  public TransitionImpl createOutgoingTransition(String transitionId) {
    TransitionImpl transition = new TransitionImpl(transitionId, processDefinition);
    transition.setSource(this);
    outgoingTransitions.add(transition);
    
    if (transitionId!=null) {
      if (namedOutgoingTransitions.containsKey(transitionId)) {
        throw new PvmException("activity '"+id+" has duplicate transition '"+transitionId+"'");
      }
      namedOutgoingTransitions.put(transitionId, transition);
    }
    
    return transition;
  }
  
  public TransitionImpl findOutgoingTransition(String transitionId) {
    return namedOutgoingTransitions.get(transitionId);
  }
  
  public String toString() {
    return "Activity("+id+")";
  }
  
  public ActivityImpl getParentActivity() {
    if (parent instanceof ActivityImpl) {
      return (ActivityImpl) parent;
    }
    return null;
  }


  // restricted setters ///////////////////////////////////////////////////////
  
  protected void setOutgoingTransitions(List<TransitionImpl> outgoingTransitions) {
    this.outgoingTransitions = outgoingTransitions;
  }

  protected void setParent(ScopeImpl parent) {
    this.parent = parent;
  }

  protected void setIncomingTransitions(List<TransitionImpl> incomingTransitions) {
    this.incomingTransitions = incomingTransitions;
  }

  // getters and setters //////////////////////////////////////////////////////

  @SuppressWarnings("unchecked")
  public List<PvmTransition> getOutgoingTransitions() {
    return (List) outgoingTransitions;
  }

  public ActivityBehavior getActivityBehavior() {
    return activityBehavior;
  }

  public void setActivityBehavior(ActivityBehavior activityBehavior) {
    this.activityBehavior = activityBehavior;
  }

  public ScopeImpl getParent() {
    return parent;
  }

  @SuppressWarnings("unchecked")
  public List<PvmTransition> getIncomingTransitions() {
    return (List) incomingTransitions;
  }

  private boolean _isScopeNullTaskContext(ActivityExecution execution,String procinstanceid)
  {
	  if(StringUtil.isEmpty(procinstanceid))
		  return this.isScope;
	  else
	  {
		  if(activityBehavior != null && this.activityBehavior instanceof MixUserTaskActivityBehavior)
		  {
			 
						
						if(this.isMultiTask(execution,procinstanceid, null))
							return true;
						else
							return false;
					
				
		  }
		  else
		  {
			  return this.isScope;
		  }
	  }
  }
  
  private boolean _isScope(ActivityExecution execution,String procinstanceid)
  {
	  if(execution != null)
	  {
		  if(execution.getTaskContext() != null)
		  {
			  if(execution.getTaskContext().isIsmulti())
				  return true;
			  else
				  return false;
		  }
		  else
		  {
			  return _isScopeNullTaskContext(execution,procinstanceid);
		  }
	  }
	  else
		  return _isScopeNullTaskContext(execution, procinstanceid);
  }
  public boolean isScope(ActivityExecution execution,String procinstanceid) {
	  
	  if(activityBehavior != null && this.activityBehavior instanceof MixUserTaskActivityBehavior)
	  {
		  return _isScope( execution, procinstanceid);
	  }
	  else
	  {
		  return this.isScope;
	  }
		  
	  
	
  }

  public void setScope(boolean isScope) {
    this.isScope = isScope;
  }

  public int getX() {
    return x;
  }
  
  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
  
  public boolean isAsync() {
    return isAsync;
  }
  
  public void setAsync(boolean isAsync) {
    this.isAsync = isAsync;
  }
  
  public boolean isExclusive() {
    return isExclusive;
  }
    
  public void setExclusive(boolean isExclusive) {
    this.isExclusive = isExclusive;
  }


  
}
