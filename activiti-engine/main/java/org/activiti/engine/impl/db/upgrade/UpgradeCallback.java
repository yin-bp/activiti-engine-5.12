package org.activiti.engine.impl.db.upgrade;

import java.util.HashMap;

public interface UpgradeCallback {
	/**
	 * 升级流程实例策略回调函数
	 * @param procdefs,包含以下信息：
	 * ID_ 流程定义id
	 * KEY_ 流程名称
	 * VERSION_ 最新流程版本号
     * DEPLOYMENT_ID_ 流程部署号
	 */
	void update(HashMap procdef) throws Exception;
	/**
	 * 删除流程实例策略回调函数
	 * @param procdefs,包含以下信息：
	 * ID_ 流程定义id
	 * KEY_ 流程名称
	 * VERSION_ 最新流程版本号
     * DEPLOYMENT_ID_ 流程部署号
	 */
	void delete(HashMap procdef) throws Exception;
}
