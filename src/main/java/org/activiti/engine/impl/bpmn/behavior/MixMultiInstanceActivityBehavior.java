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
package org.activiti.engine.impl.bpmn.behavior;

import java.util.Collection;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

/**
 * <p>Title: MixMultiInstanceActivityBehavior.java</p>
 *
 * <p>Description: 扩展Activiti功能，便于流程实例运行时串并行切换</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-5-29
 * @author biaoping.yin
 * @version 1.0
 */
public class MixMultiInstanceActivityBehavior extends
		MultiInstanceActivityBehavior {
	private MultiInstanceActivityBehavior innerParallelActivityBehavior;
	private MultiInstanceActivityBehavior innerSequentialActivityBehavior;
	
	private String defaultMultiInstanceMode = multiInstanceMode_parallel;
	private MultiInstanceActivityBehavior defaultMultiInstanceActivityBehavior;
	
	private String multiInstanceMode_variable ;
	private ActivityImpl activity;
	public MixMultiInstanceActivityBehavior(ActivityImpl activity,
			MultiInstanceActivityBehavior innerParallelActivityBehavior,MultiInstanceActivityBehavior innerSequentialActivityBehavior,String defaultMultiInstanceMode) {
		this.innerParallelActivityBehavior = innerParallelActivityBehavior;
		this.innerSequentialActivityBehavior = innerSequentialActivityBehavior;
		this.defaultMultiInstanceMode = defaultMultiInstanceMode == null || defaultMultiInstanceMode.equals("")?multiInstanceMode_parallel:defaultMultiInstanceMode;
		this.activity = activity;
		this.multiInstanceMode_variable = this.activity.getId() + multiInstanceMode_variable_const;
		if(this.defaultMultiInstanceMode.equals(multiInstanceMode_parallel))
		{
			this.defaultMultiInstanceActivityBehavior = this.innerParallelActivityBehavior;
		}
		else
		{
			this.defaultMultiInstanceActivityBehavior = this.innerSequentialActivityBehavior;
		}
		
	}
	 /**
	   * 返回任务类型是否是并行多实例任务
	   * @return
	   */
	  public boolean isParreal()
	  {
		  return defaultMultiInstanceActivityBehavior != null && defaultMultiInstanceActivityBehavior instanceof ParallelMultiInstanceBehavior;
	  }
	  
	  /**
	   * 返回任务类型是否是串行多实例任务
	   * @return
	   */
	  public boolean isSequence()
	  {
		  return defaultMultiInstanceActivityBehavior != null && defaultMultiInstanceActivityBehavior instanceof SequentialMultiInstanceBehavior;
	  }
	  
	  public boolean isMail()
	  {
		  return this.defaultMultiInstanceActivityBehavior.innerActivityBehavior instanceof MailActivityBehavior;
	  }
	private MultiInstanceActivityBehavior getMultiInstanceActivityBehavior(VariableScope execution)
	{
		String mode = (String)execution.getVariable(this.multiInstanceMode_variable);
		if(mode == null)
			mode = (String)execution.getVariableLocal(this.multiInstanceMode_variable);
		if(mode == null)
		{
			if(execution instanceof ActivityExecution)
			{
				return ((ActivityExecution)execution).getTaskContext().isIsparrel()?innerParallelActivityBehavior:innerSequentialActivityBehavior;
			}
			else
				return this.defaultMultiInstanceActivityBehavior;	
		}
		if(mode.equals(multiInstanceMode_parallel))
		{
			return this.innerParallelActivityBehavior;
		}
		else
		{
			return this.innerSequentialActivityBehavior;
		}
	}

	@Override
	protected void createInstances(ActivityExecution execution)
			throws Exception {
		getMultiInstanceActivityBehavior(execution).createInstances(execution);

	}
	 public Collection getAssignee(TaskEntity task, ActivityExecution execution)
	  {
		 return getMultiInstanceActivityBehavior(execution).getAssignee(task, execution);
//		  if (taskDefinition.getAssigneeExpression() != null) 
//		      return (String) taskDefinition.getAssigneeExpression().getValue(execution);
//		    return null;
	  }
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).execute(execution);
	}

	@Override
	public void signal(ActivityExecution execution, String signalName,
			Object signalData) throws Exception {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).signal(execution, signalName, signalData);
	}

//	@Override
//	public void signal(ActivityExecution execution, String signalName,
//			Object signalData, TaskContext taskContext) throws Exception {
//		// TODO Auto-generated method stub
//		getMultiInstanceActivityBehavior(execution).signal(execution, signalName, signalData,  taskContext);
//	}

	@Override
	public void lastExecutionEnded(ActivityExecution execution) {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).lastExecutionEnded(execution);
	}

	@Override
	public void completing(DelegateExecution execution,
			DelegateExecution subProcessInstance) throws Exception {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).completing(execution, subProcessInstance);
	}

	@Override
	public void completed(ActivityExecution execution) throws Exception {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).completed(execution);
	}

	@Override
	protected int resolveNrOfInstances(ActivityExecution execution) {
		// TODO Auto-generated method stub
		return getMultiInstanceActivityBehavior(execution).resolveNrOfInstances(execution);
	}

	@Override
	protected void executeOriginalBehavior(ActivityExecution execution,
			int loopCounter) throws Exception {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).executeOriginalBehavior(execution, loopCounter);
	}

	@Override
	protected boolean usesCollection() {
		// TODO Auto-generated method stub
		return super.usesCollection();
	}

	@Override
	protected boolean isExtraScopeNeeded() {
		// TODO Auto-generated method stub
		return super.isExtraScopeNeeded();
	}

	@Override
	protected int resolveLoopCardinality(ActivityExecution execution) {
		// TODO Auto-generated method stub
		return getMultiInstanceActivityBehavior(execution).resolveLoopCardinality(execution);
	}

	@Override
	protected boolean completionConditionSatisfied(ActivityExecution execution) {
		// TODO Auto-generated method stub
		return getMultiInstanceActivityBehavior(execution).completionConditionSatisfied(execution);
	}

	@Override
	protected void setLoopVariable(ActivityExecution execution,
			String variableName, Object value) {
		// TODO Auto-generated method stub
		super.setLoopVariable(execution, variableName, value);
		

	}

	@Override
	protected Integer getLoopVariable(ActivityExecution execution,
			String variableName) {
		// TODO Auto-generated method stub
		return super.getLoopVariable(execution, variableName);
	}

	@Override
	protected void callActivityEndListeners(ActivityExecution execution) {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).callActivityEndListeners(execution);
	}

	@Override
	protected void logLoopDetails(ActivityExecution execution, String custom,
			int loopCounter, int nrOfCompletedInstances,
			int nrOfActiveInstances, int nrOfInstances) {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).logLoopDetails(execution, custom, loopCounter, nrOfCompletedInstances,
				nrOfActiveInstances, nrOfInstances);
	}

	@Override
	public Expression getLoopCardinalityExpression() {
		// TODO Auto-generated method stub
		return super.getLoopCardinalityExpression();
	}

	@Override
	public void setLoopCardinalityExpression(
			Expression loopCardinalityExpression) {
		// TODO Auto-generated method stub
		super.setLoopCardinalityExpression(loopCardinalityExpression);
		this.innerParallelActivityBehavior.setLoopCardinalityExpression(loopCardinalityExpression);
		this.innerSequentialActivityBehavior.setLoopCardinalityExpression(loopCardinalityExpression);

	}

	@Override
	public Expression getCompletionConditionExpression() {
		// TODO Auto-generated method stub
		return super.getCompletionConditionExpression();
	}

	@Override
	public void setCompletionConditionExpression(
			Expression completionConditionExpression) {
		// TODO Auto-generated method stub
		super.setCompletionConditionExpression(completionConditionExpression);
		this.innerParallelActivityBehavior.setCompletionConditionExpression(completionConditionExpression);
		this.innerSequentialActivityBehavior.setCompletionConditionExpression(completionConditionExpression);

	}

	@Override
	public Expression getCollectionExpression() {
		// TODO Auto-generated method stub
		return super.getCollectionExpression();
	}

	@Override
	public void setCollectionExpression(Expression collectionExpression) {
		// TODO Auto-generated method stub
		super.setCollectionExpression(collectionExpression);
		this.innerParallelActivityBehavior.setCollectionExpression(collectionExpression);
		this.innerSequentialActivityBehavior.setCollectionExpression(collectionExpression);

	}

	@Override
	public String getCollectionVariable() {
		// TODO Auto-generated method stub
		return super.getCollectionVariable();
	}

	@Override
	public void setCollectionVariable(String collectionVariable) {
		// TODO Auto-generated method stub
		super.setCollectionVariable(collectionVariable);
		this.innerParallelActivityBehavior.setCollectionVariable(collectionVariable);
		this.innerSequentialActivityBehavior.setCollectionVariable(collectionVariable);
	}

	@Override
	public String getCollectionElementVariable() {
		// TODO Auto-generated method stub
		return super.getCollectionElementVariable();
	}

	@Override
	public void setCollectionElementVariable(String collectionElementVariable) {
		// TODO Auto-generated method stub
		super.setCollectionElementVariable(collectionElementVariable);
		this.innerParallelActivityBehavior.setCollectionElementVariable(collectionElementVariable);
		this.innerSequentialActivityBehavior.setCollectionElementVariable(collectionElementVariable);
	}

	/**
	 * to do
	 */
	public void setInnerActivityBehavior(
			AbstractBpmnActivityBehavior innerActivityBehavior) {
		// TODO Auto-generated method stub
		this.innerParallelActivityBehavior.setInnerActivityBehavior(innerActivityBehavior);
		this.innerSequentialActivityBehavior.setInnerActivityBehavior(innerActivityBehavior);
		super.setInnerActivityBehavior(innerActivityBehavior);
		
	}

	@Override
	protected void leave(ActivityExecution execution) {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(execution).leave(execution);
	}

//	@Override
//	protected void leave(ActivityExecution execution,TaskContext taskContext) {
//		// TODO Auto-generated method stub
//		getMultiInstanceActivityBehavior(execution).leave(execution, taskContext);
//	}

	@Override
	protected void leaveIgnoreConditions(ActivityExecution activityContext) {
		// TODO Auto-generated method stub
		getMultiInstanceActivityBehavior(activityContext).leaveIgnoreConditions(activityContext);
	}
	

}
