package bboss;

import java.util.HashMap;

import org.activiti.engine.impl.db.upgrade.UpgradeCallback;

public class TestUpgradeCallback implements UpgradeCallback {

	/**
	 * 删除流程实例策略回调函数
	 * @param procdefs,包含以下信息：
	 * ID_ 流程定义id
	 * KEY_ 流程名称
	 * VERSION_ 最新流程版本号
     * DEPLOYMENT_ID_ 流程部署号
	 */
	public void delete(HashMap procdef) throws Exception {
		System.out.println("delete to procdefid:"+procdef.get("ID_"));
		System.out.println("delete to KEY_:"+procdef.get("KEY_"));
		System.out.println("delete to VERSION_:"+procdef.get("VERSION_"));
		System.out.println("delete to DEPLOYMENT_ID_:"+procdef.get("DEPLOYMENT_ID_"));
		

	}

	/**
	 * 升级流程实例策略回调函数
	 * @param procdefs,包含以下信息：
	 * ID_ 流程定义id
	 * KEY_ 流程名称
	 * VERSION_ 最新流程版本号
     * DEPLOYMENT_ID_ 流程部署号
	 */
	public void update(HashMap procdef) throws Exception {
		System.out.println("update to procdefid:"+procdef.get("ID_"));
		System.out.println("update to KEY_:"+procdef.get("KEY_"));
		System.out.println("update to VERSION_:"+procdef.get("VERSION_"));
		System.out.println("update to DEPLOYMENT_ID_:"+procdef.get("DEPLOYMENT_ID_"));
	}

}
