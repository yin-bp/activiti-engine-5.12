package org.activiti.engine.impl.bpmn.behavior;

import org.activiti.engine.impl.TaskContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class MixUserTaskActivityBehavior extends UserTaskActivityBehavior{
	private UserTaskActivityBehavior userTaskActivityBehavior;
	private MultiInstanceActivityBehavior mixmultiInstanceActivityBehavior;
	public MultiInstanceActivityBehavior getMixmultiInstanceActivityBehavior() {
		return mixmultiInstanceActivityBehavior;
	}
	private FlowNodeActivityBehavior getFlowNodeActivityBehavior(ActivityExecution execution)
	{
		TaskContext taskContext = execution.getTaskContext();
		if(taskContext.isIsmulti())
			return mixmultiInstanceActivityBehavior;
		else
			return userTaskActivityBehavior;
	}
	public void setMixmultiInstanceActivityBehavior(
			MultiInstanceActivityBehavior mixmultiInstanceActivityBehavior) {
		this.mixmultiInstanceActivityBehavior = mixmultiInstanceActivityBehavior;
	}
	public UserTaskActivityBehavior getUserTaskActivityBehavior() {
		return userTaskActivityBehavior;
	}
	public MixUserTaskActivityBehavior(UserTaskActivityBehavior userTaskActivityBehavior) {
		super(userTaskActivityBehavior.getTaskDefinition());
		this.userTaskActivityBehavior = userTaskActivityBehavior;
	}
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		// TODO Auto-generated method stub
		getFlowNodeActivityBehavior(execution).execute(execution);
	}
	@Override
	public void signal(ActivityExecution execution, String signalName,
			Object signalData) throws Exception {
		// TODO Auto-generated method stub
		getFlowNodeActivityBehavior(execution).signal(execution, signalName, signalData);
	}
	@Override
	protected void handleAssignments(TaskEntity task,
			ActivityExecution execution) {
		// TODO Auto-generated method stub
		userTaskActivityBehavior.handleAssignments(task, execution);
	}
	
	
	@Override
	protected void leave(ActivityExecution execution) {
		// TODO Auto-generated method stub
		getFlowNodeActivityBehavior(execution).leave(execution);
	}
	@Override
	protected boolean hasCompensationHandler(ActivityExecution execution) {
		// TODO Auto-generated method stub
		return userTaskActivityBehavior.hasCompensationHandler(execution);
	}
	@Override
	protected void createCompensateEventSubscription(ActivityExecution execution) {
		// TODO Auto-generated method stub
		userTaskActivityBehavior.createCompensateEventSubscription(execution);
	}
	@Override
	protected boolean hasLoopCharacteristics(ActivityExecution execution) {
		// TODO Auto-generated method stub
		return userTaskActivityBehavior.hasLoopCharacteristics( execution);
	}
	@Override
	protected boolean hasMultiInstanceCharacteristics(ActivityExecution execution) {
		// TODO Auto-generated method stub
		return userTaskActivityBehavior.hasMultiInstanceCharacteristics( execution);
	}
	@Override
	public MultiInstanceActivityBehavior getMultiInstanceActivityBehavior() {
		// TODO Auto-generated method stub
		return userTaskActivityBehavior.getMultiInstanceActivityBehavior();
	}
	@Override
	protected void signalCompensationDone(ActivityExecution execution,
			Object signalData) {
		// TODO Auto-generated method stub
		userTaskActivityBehavior.signalCompensationDone(execution, signalData);
	}
	@Override
	protected void leaveIgnoreConditions(ActivityExecution activityContext) {
		// TODO Auto-generated method stub
		getFlowNodeActivityBehavior(activityContext).leaveIgnoreConditions(activityContext);
	}

	public String getAssignee(TaskEntity task, ActivityExecution execution)
	  {
		  
		    return userTaskActivityBehavior.getAssignee(task, execution);
	  }

}
