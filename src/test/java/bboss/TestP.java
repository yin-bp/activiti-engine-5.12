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
package bboss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.junit.Test;

/**
 * <p>Title: TestP.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-7-9
 * @author biaoping.yin
 * @version 1.0
 */
public class TestP {
	@Test
	public void test()
	{
//		ProcessEngineConfigurationImpl config = (ProcessEngineConfigurationImpl)ProcessEngineConfiguration
//				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
//				
//		ProcessEngine processEngine = config.buildProcessEngine();
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault()
                .buildProcessEngine();



        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //        repositoryService.createDeployment().addClasspathResource("Interview.bpmn20.xml").deploy();
        //
        //        repositoryService.createDeployment().addClasspathResource("Interview1.bpmn20.xml").deploy();


        repositoryService.createDeployment().addClasspathResource("bboss/test.bpmn20.xml").deploy();


        //        LatoProcessObject object = new LatoProcessObject();
        //        Map<String, Object> params = new HashMap<String, Object>();
        //        params.put("a", object);
        //        //        flash.put("a", object);
        //        processId = runtimeService.startProcessInstanceByKey("Interview", params).getId();


        //        LatoProcessObject object1 = new LatoProcessObject();
        //        Map<String, Object> params1 = new HashMap<String, Object>();
        //        params1.put("a", object1);
        //        //        flash.put("a", object);
        //        processId = runtimeService.startProcessInstanceByKey("Interview1", params1).getId();


        LatoProcessObject object = new LatoProcessObject();
        Map<String, Object> params1 = new HashMap<String, Object>();
        params1.put("a", object);
        List<String> userList = new ArrayList<String>();
        userList.add("wynn");
        userList.add("leo");
        userList.add("andy");
        userList.add("jake");
        params1.put("assigneeList", userList);
        runtimeService.startProcessInstanceByKey("appactionMyProcess", params1).getId();

	}

}
