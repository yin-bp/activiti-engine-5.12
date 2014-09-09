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
package org.activiti.engine.impl.bpmn.parser.handler;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MixMultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MixUserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.cfg.BeansConfigurationHelper;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.apache.commons.lang.StringUtils;

import com.frameworkset.util.StringUtil;


/**
 * @author Joram Barrez
 */
public abstract class AbstractActivityBpmnParseHandler<T extends FlowNode> extends AbstractFlowNodeBpmnParseHandler<T> {
  
  @Override
  public void parse(BpmnParse bpmnParse, BaseElement element) {
    super.parse(bpmnParse, element);
    
    if (element instanceof Activity
            && ((Activity) element).getLoopCharacteristics() != null) {
      createMultiInstanceLoopCharacteristics(bpmnParse, (Activity) element);
    }
    else  if(element instanceof UserTask && BeansConfigurationHelper.getProcessEngineConfiguration().enableMixMultiUserTask())
    {
    	 UserTask userTask = (UserTask)element;
    	 createUserTaskMultiInstanceLoopCharacteristics(bpmnParse, userTask) ;
    }
  }
  
  protected void createUserTaskMultiInstanceLoopCharacteristics(BpmnParse bpmnParse, UserTask modelActivity) {
	  
	 
	    
	    // Activity Behavior
	    
	    ActivityImpl activity = bpmnParse.getCurrentScope().findActivity(modelActivity.getId());
	    if (activity == null) {
	      bpmnParse.getBpmnModel().addProblem("Activity " + modelActivity.getId() + " needed for multi instance cannot bv found", modelActivity);
	    }	    
	    AbstractBpmnActivityBehavior bpmnActivityBehavior = (AbstractBpmnActivityBehavior) activity.getActivityBehavior();
	    if( !(bpmnActivityBehavior instanceof UserTaskActivityBehavior))
	    	return ;
	    String assignee = null;
	    if (StringUtil.isEmpty(modelActivity.getAssignee())) {
	    	if (StringUtil.isEmpty(modelActivity.getCandidateUsers())) {
		    	return ;
		    }
	    	else
		    {
		    	assignee = modelActivity.getCandidateUsers().get(0);
		    }
	    	
	    }
	    else
	    {
	    	assignee = modelActivity.getAssignee();
	    }
	    
	    bpmnActivityBehavior.setUseMixUsetask(true);
	    MixUserTaskActivityBehavior mixUserTaskActivityBehavior = new MixUserTaskActivityBehavior((UserTaskActivityBehavior)bpmnActivityBehavior);
//	    MultiInstanceLoopCharacteristics loopCharacteristics = modelActivity.getLoopCharacteristics();
	    MultiInstanceActivityBehavior miActivityBehavior = null;
	    MultiInstanceActivityBehavior miParallelActivityBehavior = null;
	    MultiInstanceActivityBehavior miSequentialActivityBehavior = null;
	    miParallelActivityBehavior = bpmnParse.getActivityBehaviorFactory().createParallelMultiInstanceBehavior(
	            activity, bpmnActivityBehavior);
	    miSequentialActivityBehavior = bpmnParse.getActivityBehaviorFactory().createSequentialMultiInstanceBehavior(
	            activity, bpmnActivityBehavior);
	    miActivityBehavior = new MixMultiInstanceActivityBehavior(activity, 
	    		miParallelActivityBehavior, 
	    		miSequentialActivityBehavior, 
	    		 MultiInstanceActivityBehavior.multiInstanceMode_parallel);
	    miActivityBehavior.setInnerActivityBehavior(bpmnActivityBehavior);
	    mixUserTaskActivityBehavior.setMixmultiInstanceActivityBehavior(miActivityBehavior);
//	    if (loopCharacteristics.isSequential()) {
//	      miActivityBehavior = bpmnParse.getActivityBehaviorFactory().createSequentialMultiInstanceBehavior(
//	              activity, (AbstractBpmnActivityBehavior) activity.getActivityBehavior()); 
//	    } else {
//	      miActivityBehavior = bpmnParse.getActivityBehaviorFactory().createParallelMultiInstanceBehavior(
//	              activity, (AbstractBpmnActivityBehavior) activity.getActivityBehavior());
//	    }
	    
	    // ActivityImpl settings
	    activity.setScope(true);//scope主要是用来干嘛呢
//	    activity.setProperty("multiInstance", loopCharacteristics.isSequential() ? MultiInstanceActivityBehavior.multiInstanceMode_sequential : MultiInstanceActivityBehavior.multiInstanceMode_parallel);
	    activity.setActivityBehavior(mixUserTaskActivityBehavior);
	    
	    ExpressionManager expressionManager = bpmnParse.getExpressionManager();
	    BpmnModel bpmnModel = bpmnParse.getBpmnModel();
	    
//	    // loopcardinality
//	    if (StringUtils.isNotEmpty(loopCharacteristics.getLoopCardinality())) {
//	      miActivityBehavior.setLoopCardinalityExpression(expressionManager.createExpression(loopCharacteristics.getLoopCardinality()));
//	    }
//	    
//	    // completion condition
//	    if (StringUtils.isNotEmpty(loopCharacteristics.getCompletionCondition())) {
//	      miActivityBehavior.setCompletionConditionExpression(expressionManager.createExpression(loopCharacteristics.getCompletionCondition()));
//	    }
	    
	    // activiti:collection
//	    if (StringUtils.isNotEmpty(modelActivity.getAssignee())) {
	      if (modelActivity.getAssignee().contains("{")) {
	        miActivityBehavior.setCollectionExpression(expressionManager.createExpression(assignee));
	       
	       
	      } else {
	        miActivityBehavior.setCollectionVariable(assignee);
	       
	      }
	      miActivityBehavior.setCollectionElementVariable(modelActivity.getId()+"_user");
	      bpmnActivityBehavior.setCollectionElementVariable(miActivityBehavior.getCollectionElementVariable());
//	    }

	    
	      
	   

	    

	  }
  
  protected void createMultiInstanceLoopCharacteristics(BpmnParse bpmnParse, org.activiti.bpmn.model.Activity modelActivity) {
    
    MultiInstanceLoopCharacteristics loopCharacteristics = modelActivity.getLoopCharacteristics();
    
    // Activity Behavior
    MultiInstanceActivityBehavior miActivityBehavior = null;
    MultiInstanceActivityBehavior miParallelActivityBehavior = null;
    MultiInstanceActivityBehavior miSequentialActivityBehavior = null;
    ActivityImpl activity = bpmnParse.getCurrentScope().findActivity(modelActivity.getId());
    if (activity == null) {
      bpmnParse.getBpmnModel().addProblem("Activity " + modelActivity.getId() + " needed for multi instance cannot bv found", modelActivity);
    }
    AbstractBpmnActivityBehavior bpmnActivityBehavior = (AbstractBpmnActivityBehavior) activity.getActivityBehavior();
    miParallelActivityBehavior = bpmnParse.getActivityBehaviorFactory().createParallelMultiInstanceBehavior(
            activity, bpmnActivityBehavior);
    miSequentialActivityBehavior = bpmnParse.getActivityBehaviorFactory().createSequentialMultiInstanceBehavior(
            activity, bpmnActivityBehavior);
    miActivityBehavior = new MixMultiInstanceActivityBehavior(activity, 
    		miParallelActivityBehavior, 
    		miSequentialActivityBehavior, 
    		loopCharacteristics.isSequential()?MultiInstanceActivityBehavior.multiInstanceMode_sequential : MultiInstanceActivityBehavior.multiInstanceMode_parallel);
    miActivityBehavior.setInnerActivityBehavior(bpmnActivityBehavior);
//    if (loopCharacteristics.isSequential()) {
//      miActivityBehavior = bpmnParse.getActivityBehaviorFactory().createSequentialMultiInstanceBehavior(
//              activity, (AbstractBpmnActivityBehavior) activity.getActivityBehavior()); 
//    } else {
//      miActivityBehavior = bpmnParse.getActivityBehaviorFactory().createParallelMultiInstanceBehavior(
//              activity, (AbstractBpmnActivityBehavior) activity.getActivityBehavior());
//    }
    
    // ActivityImpl settings
    activity.setScope(true);
    activity.setProperty("multiInstance", loopCharacteristics.isSequential() ? MultiInstanceActivityBehavior.multiInstanceMode_sequential : MultiInstanceActivityBehavior.multiInstanceMode_parallel);
    activity.setActivityBehavior(miActivityBehavior);
    
    ExpressionManager expressionManager = bpmnParse.getExpressionManager();
    BpmnModel bpmnModel = bpmnParse.getBpmnModel();
    
    // loopcardinality
    if (StringUtils.isNotEmpty(loopCharacteristics.getLoopCardinality())) {
      miActivityBehavior.setLoopCardinalityExpression(expressionManager.createExpression(loopCharacteristics.getLoopCardinality()));
    }
    
    // completion condition
    if (StringUtils.isNotEmpty(loopCharacteristics.getCompletionCondition())) {
      miActivityBehavior.setCompletionConditionExpression(expressionManager.createExpression(loopCharacteristics.getCompletionCondition()));
    }
    
    // activiti:collection
    if (StringUtils.isNotEmpty(loopCharacteristics.getInputDataItem())) {
      if (loopCharacteristics.getInputDataItem().contains("{")) {
        miActivityBehavior.setCollectionExpression(expressionManager.createExpression(loopCharacteristics.getInputDataItem()));
      } else {
        miActivityBehavior.setCollectionVariable(loopCharacteristics.getInputDataItem());
      }
    }

    // activiti:elementVariable
    if (StringUtils.isNotEmpty(loopCharacteristics.getElementVariable())) {
      miActivityBehavior.setCollectionElementVariable(loopCharacteristics.getElementVariable());
      if(modelActivity instanceof UserTask)
      {
    	  UserTask userTask = (UserTask)modelActivity;
    	  if(StringUtils.isEmpty(userTask.getAssignee()))
    	  {
    		  userTask.setAssignee("${"+loopCharacteristics.getElementVariable() + "}");
    		  TaskDefinition taskDefinition = bpmnParse.getCurrentProcessDefinition().getTaskDefinition(userTask.getId());
    		  if(taskDefinition.getAssigneeExpression() == null && taskDefinition.getCandidateGroupIdExpressions().isEmpty() && taskDefinition.getCandidateUserIdExpressions().isEmpty())
    		  {
    			  taskDefinition.setAssigneeExpression(expressionManager.createExpression(userTask.getAssignee()));
    		  }
    	  }
      }
      
    }

    // Validation
    if (miActivityBehavior.getLoopCardinalityExpression() == null && miActivityBehavior.getCollectionExpression() == null
            && miActivityBehavior.getCollectionVariable() == null) {
      bpmnModel.addProblem("Either loopCardinality or loopDataInputRef/activiti:collection must been set.", loopCharacteristics);
    }

    // Validation
    if (miActivityBehavior.getCollectionExpression() == null && miActivityBehavior.getCollectionVariable() == null
            && miActivityBehavior.getCollectionElementVariable() != null) {
      bpmnModel.addProblem("LoopDataInputRef/activiti:collection must be set when using inputDataItem or activiti:elementVariable.", loopCharacteristics);
    }

  }

}
