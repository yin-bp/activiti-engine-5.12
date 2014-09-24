/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.persistence.entity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;

/**
 * @author Christian Stettler
 */
public class HistoricActivityInstanceEntity extends HistoricScopeInstanceEntity implements HistoricActivityInstance {

public HistoricActivityInstanceEntity()
{
	super();
}
  private static final long serialVersionUID = 1L;
  
  protected String activityId;
  protected String activityName;
  protected String activityType;
  protected String executionId;
  protected String assignee;
  protected String taskId;
  protected String calledProcessInstanceId;
  /**
   * 超时是否已经发送
   */
  protected int OVERTIMESEND;
  /**
   * '任务持续时间限制
   */
  protected long DURATION_NODE;
  /**
   * 预警是否已发送
   */
  protected int ADVANCESEND;
  /**
   * 提前预警时间
   */
  protected Timestamp ALERTTIME;
  
  /**
   * 超时告警时间
   */
  protected Timestamp OVERTIME;
  /**
   * 预警时间率
   */
  protected int NOTICERATE;
  
  /**
   * 节假日策略
   * '节假日策略，1-考虑节假日，不考虑作息时间，0-不考虑节假日，不考虑作息时间，2-考虑节假日，考虑作息时间，默认值为1';
   */
  protected int IS_CONTAIN_HOLIDAY;
  
  protected int autoComplete;
  protected String autoCompleteHandler;
  
  public Object getPersistentState() {
    Map<String, Object> persistentState = (Map<String, Object>) new HashMap<String, Object>();
    persistentState.put("endTime", endTime);
    persistentState.put("durationInMillis", durationInMillis);
    persistentState.put("deleteReason", deleteReason);
    persistentState.put("executionId", executionId);
    persistentState.put("assignee", assignee);
    
    persistentState.put("owner", owner);    
    
    if (claimTime != null) {
      persistentState.put("claimTime", claimTime);
    }
    if(this.bussinessOperation != null)
    	persistentState.put("bussinessOperation", bussinessOperation);
    if(this.bussinessRemark != null)
    {
    	persistentState.put("bussinessRemark", bussinessRemark);
    }
    
    return persistentState;
  }

  // getters and setters //////////////////////////////////////////////////////
  
  public String getActivityId() {
    return activityId;
  }
  public String getActivityName() {
    return activityName;
  }

  public String getActivityType() {
    return activityType;
  }
  
  public String getExecutionId() {
    return executionId;
  }
  
  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }
  
  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }
  
  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }
  
  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }

  public String getAssignee() {
    return assignee;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public String getTaskId() {
    return taskId;
  }
  
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getCalledProcessInstanceId() {
    return calledProcessInstanceId;
  }
  
  public void setCalledProcessInstanceId(String calledProcessInstanceId) {
    this.calledProcessInstanceId = calledProcessInstanceId;
  }

public int getOVERTIMESEND() {
	return OVERTIMESEND;
}

public void setOVERTIMESEND(int oVERTIMESEND) {
	OVERTIMESEND = oVERTIMESEND;
}

public long getDURATION_NODE() {
	return DURATION_NODE;
}

public void setDURATION_NODE(long dURATION_NODE) {
	DURATION_NODE = dURATION_NODE;
}

public int getADVANCESEND() {
	return ADVANCESEND;
}

public void setADVANCESEND(int aDVANCESEND) {
	ADVANCESEND = aDVANCESEND;
}

public Timestamp getALERTTIME() {
	return ALERTTIME;
}

public void setALERTTIME(Timestamp aLERTTIME) {
	ALERTTIME = aLERTTIME;
}

public Timestamp getOVERTIME() {
	return OVERTIME;
}

public void setOVERTIME(Timestamp oVERTIME) {
	OVERTIME = oVERTIME;
}

public int getNOTICERATE() {
	return NOTICERATE;
}

public void setNOTICERATE(int nOTICERATE) {
	NOTICERATE = nOTICERATE;
}

public int getIS_CONTAIN_HOLIDAY() {
	return IS_CONTAIN_HOLIDAY;
}

public void setIS_CONTAIN_HOLIDAY(int iS_CONTAIN_HOLIDAY) {
	IS_CONTAIN_HOLIDAY = iS_CONTAIN_HOLIDAY;
}

public int getAutoComplete() {
	return autoComplete;
}

public void setAutoComplete(int autoComplete) {
	this.autoComplete = autoComplete;
}

public String getAutoCompleteHandler() {
	return autoCompleteHandler;
}

public void setAutoCompleteHandler(String autoCompleteHandler) {
	this.autoCompleteHandler = autoCompleteHandler;
}

}
