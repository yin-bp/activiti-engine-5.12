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
package org.activiti.engine.impl.pvm.runtime;

import java.util.List;

import org.activiti.engine.impl.TaskContext;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Tom Baeyens
 */
public class AtomicOperationTransitionDestroyScope implements AtomicOperation {
  
  private static Logger log = LoggerFactory.getLogger(AtomicOperationTransitionDestroyScope.class);
  
  public boolean isAsync(InterpretableExecution execution) {
    return false;
  }

  @SuppressWarnings("unchecked")
  public void execute(InterpretableExecution execution) {
    InterpretableExecution propagatingExecution = null;

    ActivityImpl activity = (ActivityImpl) execution.getActivity();
    boolean refreshTaskContext = false;
    // if this transition is crossing a scope boundary
    if (activity.isScope(execution,execution.getProcessInstanceId())) {
//      if(execution.getTaskContext().isIsmulti())
      
	      InterpretableExecution parentScopeInstance = null;
	      // if this is a concurrent execution crossing a scope boundary
	      if (execution.isConcurrent() && !execution.isScope()) {
	        // first remove the execution from the current root
	        InterpretableExecution concurrentRoot = (InterpretableExecution) execution.getParent();
	        parentScopeInstance = (InterpretableExecution) execution.getParent().getParent();
	
	        log.debug("moving concurrent {} one scope up under {}", execution, parentScopeInstance);
	        List<InterpretableExecution> parentScopeInstanceExecutions = (List<InterpretableExecution>) parentScopeInstance.getExecutions();
	        List<InterpretableExecution> concurrentRootExecutions = (List<InterpretableExecution>) concurrentRoot.getExecutions();
	        // if the parent scope had only one single scope child
	        if (parentScopeInstanceExecutions.size()==1) {
	          // it now becomes a concurrent execution
	          parentScopeInstanceExecutions.get(0).setConcurrent(true);
	        }
	        
	        concurrentRootExecutions.remove(execution);
	        parentScopeInstanceExecutions.add(execution);
	        execution.setParent(parentScopeInstance);
	        execution.setActivity(activity);
	        propagatingExecution = execution;
	        
	        // if there is only a single concurrent execution left
	        // in the concurrent root, auto-prune it.  meaning, the 
	        // last concurrent child execution data should be cloned into
	        // the concurrent root.   
	        if (concurrentRootExecutions.size()==1) {
	          InterpretableExecution lastConcurrent = concurrentRootExecutions.get(0);
	          if (lastConcurrent.isScope()) {
	            lastConcurrent.setConcurrent(false);
	            
	          } else {
	            log.debug("merging last concurrent {} into concurrent root {}", lastConcurrent, concurrentRoot);
	            
	            // We can't just merge the data of the lastConcurrent into the concurrentRoot.
	            // This is because the concurrent root might be in a takeAll-loop.  So the 
	            // concurrent execution is the one that will be receiving the take
	            concurrentRoot.setActivity((ActivityImpl) lastConcurrent.getActivity());
	            concurrentRoot.setActive(lastConcurrent.isActive());
	            lastConcurrent.setReplacedBy(concurrentRoot);
	            lastConcurrent.remove();
	          }
	        }
	
	      } else if (execution.isConcurrent() && execution.isScope()) {
	        log.debug("scoped concurrent {} becomes concurrent and remains under {}", execution, execution.getParent());
	
	        // TODO!
	        execution.destroy();
	        propagatingExecution = execution;
	        
	      } else {
	        propagatingExecution = (InterpretableExecution) execution.getParent();
	        propagatingExecution.setActivity((ActivityImpl) execution.getActivity());
	        propagatingExecution.setTransition(execution.getTransition());
	        TaskContext taskContext = execution.getTaskContext();
	        propagatingExecution.setActive(true);
	        TaskContext taskContext1 = propagatingExecution.getTaskContext();
	        if(taskContext1 != null)
	        {
	        	if(taskContext != null)
	        	{
			        taskContext1.setIsrejected(taskContext.isIsrejected());
			        taskContext1.setIswithdraw(taskContext.isIswithdraw());
			        taskContext1.setIsjump(taskContext.isIsjump());
			        taskContext1.setOp(taskContext.getOp());
			        taskContext1.setReturntoreject(taskContext.isReturntoreject());
			        taskContext1.setRejecttype(taskContext.getRejecttype());
			        taskContext1.setRejectedtaskid(taskContext.getRejectedtaskid());
			        taskContext1.setRejectednode(taskContext.getRejectednode());
			        refreshTaskContext = true;
	        	}
	        }
	        else
	        {
	        	if(taskContext != null)
	        		propagatingExecution.setTaskContext(taskContext);
	        }
	        log.debug("destroy scope: scoped {} continues as parent scope {}", execution, propagatingExecution);
	        execution.destroy();
	        execution.remove();
	      }
      
//      else
//      {
//    	  propagatingExecution = execution;
//      }
      
    } else {
      propagatingExecution = execution;
    }
    
    // if there is another scope element that is ended
    ScopeImpl nextOuterScopeElement = activity.getParent();
    TransitionImpl transition = propagatingExecution.getTransition();
    ActivityImpl destination = transition.getDestination();
    if (transitionLeavesNextOuterScope(nextOuterScopeElement, destination)) {
      propagatingExecution.setActivity((ActivityImpl) nextOuterScopeElement);
      propagatingExecution.performOperation(TRANSITION_NOTIFY_LISTENER_END);
    } else {
//    	ActivityImpl source = (ActivityImpl)propagatingExecution.getActivity();
//      if(destination.isUserTask())
//      {
//    	  
//    	  if(!refreshTaskContext){
//    		  if( source.getId().equals(destination.getId()))
//    		{
//    		  TaskContext taskContext1 = propagatingExecution.getTaskContext();
//    		  
//    		  
//    		  TaskContext newTaskContext = Context.createTaskContext((ExecutionEntity) propagatingExecution, destination.getId());
//    		  if(taskContext1 != null)
//    		  {
//    			  if(taskContext1.isIsrejected())
//    			  {
//    				  newTaskContext.setIsrejected(taskContext1.isIsrejected());
//    				  newTaskContext.setReturntoreject(taskContext1.isReturntoreject());
//    				  newTaskContext.setRejecttype(taskContext1.getRejecttype());
//    				  newTaskContext.setRejectedtaskid(taskContext1.getRejectedtaskid());
//    				  newTaskContext.setRejectednode(taskContext1.getRejectednode());
//    			      refreshTaskContext = true;
//    			  }
//    		  }
//    		}
//    	  }
//    	  
//    	 
//      }
//      else
//      {
//    	  propagatingExecution.setTaskContext(null);
//      }
      propagatingExecution.performOperation(TRANSITION_NOTIFY_LISTENER_TAKE);
    }
  }

  public boolean transitionLeavesNextOuterScope(ScopeImpl nextScopeElement, ActivityImpl destination) {
    return !nextScopeElement.contains(destination);
  }
}
