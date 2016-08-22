package org.activiti.engine.impl.bpmn.behavior;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class CopyTaskBehavior  implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		ExecutionEntity executionEntity = (ExecutionEntity)execution;
		if(executionEntity.getTaskContext().isCOPY())
		{
			executionEntity.setBussinessop("抄送");
			executionEntity.setDeleteReason("抄送");
			executionEntity.setBussinessRemark("抄送:"+executionEntity.getTaskContext().getCopyersCNName());
		}
		else if(executionEntity.getTaskContext().isNotify())
		{
			executionEntity.setBussinessop("通知");
			executionEntity.setDeleteReason("通知");
			executionEntity.setBussinessRemark("通知:"+executionEntity.getTaskContext().getCopyersCNName());
		}
		else
		{
			executionEntity.setBussinessop("抄送");
			executionEntity.setDeleteReason("抄送");
			executionEntity.setBussinessRemark("抄送:"+executionEntity.getTaskContext().getCopyersCNName());
		}
		
		Context.getProcessEngineConfiguration().getTaskService().createCopyTasks(executionEntity);
		
		
		/**
		 *  #[copertype],
					     #[coper],
					     #[process_id],
					     #[process_key],
					     #[businesskey],
					     #[copytime],
					     #[actid])
		 */
		
	}

}
