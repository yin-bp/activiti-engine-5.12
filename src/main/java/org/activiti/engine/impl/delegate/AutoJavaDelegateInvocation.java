package org.activiti.engine.impl.delegate;

import org.activiti.engine.delegate.AutoJavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;

public class AutoJavaDelegateInvocation extends DelegateInvocation {

	  protected final AutoJavaDelegate delegateInstance;
	  protected final DelegateExecution execution;

	  public AutoJavaDelegateInvocation(AutoJavaDelegate delegateInstance, DelegateExecution execution) {
	    this.delegateInstance = delegateInstance;
	    this.execution = execution;
	  }

	  protected void invoke() throws Exception {
	    delegateInstance.autoexecute((DelegateExecution) execution);
	  }
	  
	  public Object getTarget() {
	    return delegateInstance;
	  }

}
