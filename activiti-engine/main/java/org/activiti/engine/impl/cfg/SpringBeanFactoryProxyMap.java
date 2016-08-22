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

package org.activiti.engine.impl.cfg;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.SPIException;


/**
 * @author Tom Baeyens
 */
public class SpringBeanFactoryProxyMap implements Map<Object, Object> {

	protected BaseApplicationContext beanFactory;
	  
	  protected BaseApplicationContext otherbeanFactory;
	  
	  public SpringBeanFactoryProxyMap(BaseApplicationContext beanFactory,BaseApplicationContext otherbeanFactory) {
	    this.beanFactory = beanFactory;
	    this.otherbeanFactory = otherbeanFactory;
	  }

	  public Object get(Object key) {
	    if ( (key==null) || (!String.class.isAssignableFrom(key.getClass())) ) {
	      return null;
	    }
	    Object value = null;
	    try
	    {
	    
	    	value = otherbeanFactory.getBeanObject((String) key);
	    }
	    catch(SPIException e)
	    {
		    try {
				if(value == null && beanFactory != null)
					value = beanFactory.getBeanObject((String) key);
			} catch (SPIException e1) {
				
				throw new SPIException(new SPIException[]{e,e1});
			}
		    
	    }
	    return value;
	  }

	  public boolean containsKey(Object key) {
	    if ( (key==null) || (!String.class.isAssignableFrom(key.getClass())) ) {
	      return false;
	    }
	    if(otherbeanFactory != null)
	    {
	    	return otherbeanFactory.containsBean((String) key) || beanFactory.containsBean((String) key);
	    }
	    else
	    {
	    	return beanFactory.containsBean((String) key);
	    }
	  }


  public Set<Object> keySet() {
    throw new ActivitiException("unsupported operation on configuration beans");
//    List<String> beanNames = Arrays.asList(beanFactory.getBeanDefinitionNames());
//    return new HashSet<Object>(beanNames);
  }

  public void clear() {
    throw new ActivitiException("can't clear configuration beans");
  }

  public boolean containsValue(Object value) {
    throw new ActivitiException("can't search values in configuration beans");
  }

  public Set<java.util.Map.Entry<Object, Object>> entrySet() {
    throw new ActivitiException("unsupported operation on configuration beans");
  }

  public boolean isEmpty() {
    throw new ActivitiException("unsupported operation on configuration beans");
  }

  public Object put(Object key, Object value) {
    throw new ActivitiException("unsupported operation on configuration beans");
  }

  public void putAll(Map< ? extends Object, ? extends Object> m) {
    throw new ActivitiException("unsupported operation on configuration beans");
  }

  public Object remove(Object key) {
    throw new ActivitiException("unsupported operation on configuration beans");
  }

  public int size() {
    throw new ActivitiException("unsupported operation on configuration beans");
  }

  public Collection<Object> values() {
    throw new ActivitiException("unsupported operation on configuration beans");
  }
}
