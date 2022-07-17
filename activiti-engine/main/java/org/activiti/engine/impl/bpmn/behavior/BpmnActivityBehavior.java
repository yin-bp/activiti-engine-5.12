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

package org.activiti.engine.impl.bpmn.behavior;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.TaskContext;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for implementing BPMN 2.0 activities, offering convenience
 * methods specific to BPMN 2.0.
 * 
 * This class can be used by inheritance or aggregation.
 * 
 * @author Joram Barrez
 */
public class BpmnActivityBehavior implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private static Logger log = LoggerFactory.getLogger(BpmnActivityBehavior.class);

  /**
   * Performs the default outgoing BPMN 2.0 behavior, which is having parallel
   * paths of executions for the outgoing sequence flow.
   * 
   * More precisely: every sequence flow that has a condition which evaluates to
   * true (or which doesn't have a condition), is selected for continuation of
   * the process instance. If multiple sequencer flow are selected, multiple,
   * parallel paths of executions are created.
   */
  public void performDefaultOutgoingBehavior(ActivityExecution activityExceution) {
    performOutgoingBehavior(activityExceution, true, false, null);
  }
  
  
//  public void performDefaultOutgoingBehavior(ActivityExecution activityExceution,TaskContext taskContext) {
//	    performOutgoingBehavior(activityExceution, true, false, null,taskContext);
//	  }
  /**
   * Performs the default outgoing BPMN 2.0 behavior (@see
   * {@link #performDefaultOutgoingBehavior(ActivityExecution)}), but without
   * checking the conditions on the outgoing sequence flow.
   * 
   * This means that every outgoing sequence flow is selected for continuing the
   * process instance, regardless of having a condition or not. In case of
   * multiple outgoing sequence flow, multiple parallel paths of executions will
   * be created.
   */
  public void performIgnoreConditionsOutgoingBehavior(ActivityExecution activityExecution) {
    performOutgoingBehavior(activityExecution, false, false, null);
  }

  /**
   * Actual implementation of leaving an activity.
   * 
   * @param execution
   *          The current execution context
   * @param checkConditions
   *          Whether or not to check conditions before determining whether or
   *          not to take a transition.
   * @param throwExceptionIfExecutionStuck
   *          If true, an {@link ActivitiException} will be thrown in case no
   *          transition could be found to leave the activity.

//  protected void performOutgoingBehavior(ActivityExecution execution, 
//          boolean checkConditions, boolean throwExceptionIfExecutionStuck, List<ActivityExecution> reusableExecutions)
//  {
//	  performOutgoingBehavior(execution, 
//	          checkConditions, throwExceptionIfExecutionStuck, reusableExecutions,null);
//  }

   * added by biaoping.yin
   * @param execution
   * @param checkConditions
   * @param throwExceptionIfExecutionStuck
   * @param reusableExecutions
   * @param destinationTaskKey
   */
  protected void performOutgoingBehavior(ActivityExecution execution, 
          boolean checkConditions, boolean throwExceptionIfExecutionStuck, List<ActivityExecution> reusableExecutions) {

    if (log.isDebugEnabled()) {
      log.debug("Leaving activity '{}'", execution.getActivity().getId());
    }
    TaskContext taskContext = execution.getTaskContext();
    if(taskContext!=null &&  taskContext.getDestinationTaskKey() != null)
    {
    	TransitionImpl transition = ((ActivityImpl)execution.getActivity()).createCustomOutgoingTransition(null, taskContext.getDestinationTaskKey());
    	execution.take(transition);
    }
    else
    {
	    String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
	    List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();
	
	    List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
	    for (PvmTransition outgoingTransition : outgoingTransitions) {
	      if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
	        Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
	        if (condition == null || !checkConditions || condition.evaluate(execution)) {
	          transitionsToTake.add(outgoingTransition);
	        }
	      }
	    }
	
	    if (transitionsToTake.size() == 1) {
	      
	      execution.take(transitionsToTake.get(0));
	
	    } else if (transitionsToTake.size() >= 1) {
	
	      execution.inactivate();
	      if (reusableExecutions == null || reusableExecutions.isEmpty()) {
	        execution.takeAll(transitionsToTake, Arrays.asList(execution));
	      } else {
	        execution.takeAll(transitionsToTake, reusableExecutions);
	      }
	
	    } else {
	
	      if (defaultSequenceFlow != null) {
	        PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
	        if (defaultTransition != null) {
	          execution.take(defaultTransition);
	        } else {
	          throw new ActivitiException("Default sequence flow '" + defaultSequenceFlow + "' could not be not found");
	        }
	      } else {
	        
	        Object isForCompensation = execution.getActivity().getProperty(BpmnParse.PROPERTYNAME_IS_FOR_COMPENSATION);
	        if(isForCompensation != null && (Boolean) isForCompensation) {
	          
	          InterpretableExecution parentExecution = (InterpretableExecution) execution.getParent();
	          ((InterpretableExecution)execution).remove();
	          parentExecution.signal("compensationDone", null);            
	          
	        } else {
	          
	          if (log.isDebugEnabled()) {
	            log.debug("No outgoing sequence flow found for {}. Ending execution.", execution.getActivity().getId());
	          }
	          execution.end();
	          
	          if (throwExceptionIfExecutionStuck) {
	            throw new ActivitiException("No outgoing sequence flow of the inclusive gateway '" + execution.getActivity().getId()
	                  + "' could be selected for continuing the process");
	          }
	        }
	        
	      }
	    }
    }
    
  }

/**
 * 解决多实例任务没有执行处理流出路径的条件运算问题
 * @param execution
 * @param executionEntity
 * @return
 */
protected  List<PvmTransition> evalOutgoingTransition(ActivityExecution execution, ExecutionEntity executionEntity) {
	
    List<PvmTransition> outgoingTransitions = executionEntity.getActivity().getOutgoingTransitions();
//    List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>(pts) ;
    String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
    
	    List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();
	    PvmTransition defaultPvmTransition = null;
//	    List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
	    for (PvmTransition outgoingTransition : outgoingTransitions) {
	      if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
	        Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
	        if (condition == null || condition.evaluate(execution)) {
	          transitionsToTake.add(outgoingTransition);
	        }
	      }
	      else 
	      {
	    	  defaultPvmTransition =  outgoingTransition;
	      }
	    }
	    
	    if(transitionsToTake.size() == 0 && defaultPvmTransition != null)
	    {
	    	transitionsToTake.add(defaultPvmTransition);
	    }
	    return transitionsToTake;
	
}

}
