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

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.CommandContextInterceptor;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.interceptor.CommandInterceptor;



/**
 * @author Tom Baeyens
 */
public class ServiceImpl {

  protected CommandExecutor commandExecutor;
  protected ProcessEngineConfigurationImpl processEngineConfiguration;
  public CommandExecutor getCommandExecutor() {
    return commandExecutor;
  }

  public void setCommandExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
  }
  public ProcessEngineConfigurationImpl findProcessEngineConfigurationImpl()
  {
	  if(processEngineConfiguration != null)
		  return processEngineConfiguration;
	  CommandInterceptor temp = (CommandInterceptor)commandExecutor;
	  do
	  {
		  if(temp instanceof CommandContextInterceptor)
		  {
			  this.processEngineConfiguration = ((CommandContextInterceptor)temp).getProcessEngineConfiguration();
			  break;
		  }
		  temp = (CommandInterceptor)temp.getNext();
	  }while(temp !=null);
	  return processEngineConfiguration;
  }
}
