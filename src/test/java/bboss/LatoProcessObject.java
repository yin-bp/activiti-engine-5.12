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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

/**
 * <p>Title: LatoProcessObject.java</p>
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
public class LatoProcessObject implements Serializable
{
	 //    private static String processId;
    //
    //    public LatoProcessObject(String processId)
    //    {
    //        this.processId = processId;
    //    }


    public static Boolean checkOk(String sequenceFlowId)
    {
        //        Logger.info("checkOk processId :" + sequenceFlowId);
        //        Logger.info("checkOk sequenceFlowId :" + sequenceFlowId);
        //业务
        return true;
    }


    public static Boolean checkOk1(String sequenceFlowId)
    {
        //        Logger.info("checkOk processId :" + sequenceFlowId);
        //        Logger.info("checkOk sequenceFlowId :" + sequenceFlowId);
        //业务
        return false;
    }


    public static Boolean canComplete(ActivityExecution execution)
    {
        String actInstId = execution.getProcessInstanceId();


        //        Logger.info("executionId:" + execution.getId());
        //        Logger.info("processInstanceId:" + actInstId);
        String assignee = execution.getVariable("assignee").toString();
        //        Logger.info("assignee:" + assignee);
        //        ProcessDefinition processDefinition = bpmService.getProcessDefinitionByProcessInanceId(actInstId);
        //        List<String> userIds=(List<String>)execution.getVariable(BpmConst.SUBPRO_MULTI_USERIDS);
        return true;
    }


    public static List<String> getUsers(ActivityExecution execution)
    {
        String executionId = execution.getId();
        //        Logger.info("executionId:" + executionId);


        List<String> users = new ArrayList<String>();
        users.add("wynn");
        users.add("leo");
        users.add("bate");
        users.add("andy");


        execution.setVariable("userList", users);


        return users;
    }

}
