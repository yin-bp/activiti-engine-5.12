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
package org.activiti.engine.impl.form;

import org.activiti.engine.form.AbstractFormType;

/**
 * <p>Title: UserFormType.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-6-6
 * @author biaoping.yin
 * @version 1.0
 */
public class UserFormType  extends AbstractFormType {

	  public String getName() {
	    return "user";
	  }

	  public String getMimeType() {
	    return "text/plain";
	  }

	  public Object convertFormValueToModelValue(String propertyValue) {
	    return propertyValue;
	  }

	  public String convertModelValueToFormValue(Object modelValue) {
	    return (String) modelValue;
	  }

}
