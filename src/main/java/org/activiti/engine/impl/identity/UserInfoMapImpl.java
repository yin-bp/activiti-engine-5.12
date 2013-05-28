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
package org.activiti.engine.impl.identity;

/**
 * <p>Title: UserInfoMapImpl.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-5-28
 * @author biaoping.yin
 * @version 1.0
 */
public class UserInfoMapImpl implements UserInfoMap {

	@Override
	public String getUserName(String userAccount) {		
		return userAccount;
	}

	@Override
	public Object getUserAttribute(String userAccount, String userAttribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserAttributeByID(String userID, String userAttribute) {
		// TODO Auto-generated method stub
		return null;
	}

}
