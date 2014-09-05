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

import java.net.URL;

import org.activiti.engine.ProcessEngineConfiguration;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;


/**
 * @author Tom Baeyens
 */
public class BeansConfigurationHelper {
	private static BaseApplicationContext  configBeanFactory = null; 
	private static ProcessEngineConfiguration  processEngineConfiguration = null;
	public static BaseApplicationContext getConfigBeanFactory() {
		return configBeanFactory;
	}


	//  public static ProcessEngineConfiguration parseProcessEngineConfiguration(Resource springResource, String beanName) {
//    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//    XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
//    xmlBeanDefinitionReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
//    xmlBeanDefinitionReader.loadBeanDefinitions(springResource);
//    ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) beanFactory.getBean(beanName);
//    processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(beanFactory));
//    return processEngineConfiguration;
//  }
	 public static ProcessEngineConfiguration parseProcessEngineConfiguration(String bbossResource, String beanName) {
//	    DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//	    XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
//	    xmlBeanDefinitionReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
//	    xmlBeanDefinitionReader.loadBeanDefinitions(springResource);
		DefaultApplicationContext  beanFactory = DefaultApplicationContext.getApplicationContext(bbossResource);
		configBeanFactory = beanFactory;
	    ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) beanFactory.getBeanObject(beanName);
	    BaseApplicationContext beanFactory_ =  beanFactory.getTBeanObject("beanFactory", BaseApplicationContext.class);
	    if(beanFactory_ != null)    
	    	processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(beanFactory,beanFactory_));
	    else
	    {
	    	processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(beanFactory,null));
	    }
	    return BeansConfigurationHelper.processEngineConfiguration = processEngineConfiguration;
	  }
//  public static ProcessEngineConfiguration parseProcessEngineConfigurationFromInputStream(InputStream inputStream, String beanName) {
//    Resource springResource = new InputStreamResource(inputStream);
//    return parseProcessEngineConfiguration(springResource, beanName);
//  }
	 
	 
	 //
	   public static ProcessEngineConfiguration parseProcessEngineConfigurationFromInputStream(URL inputStream, String beanName) {
	 	  DefaultApplicationContext  beanFactory = DefaultApplicationContext.getApplicationContext(inputStream);
	 	    ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) beanFactory.getBeanObject(beanName);
	 	    BaseApplicationContext beanFactory_ =  beanFactory.getTBeanObject("beanFactory", BaseApplicationContext.class);
	 	    if(beanFactory_ != null)    
	 	    	processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(beanFactory,beanFactory_));
	 	    else
	 	    {
	 	    	processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(beanFactory,null));
	 	    }
	 	    return processEngineConfiguration;
//	     return parseProcessEngineConfiguration(springResource, beanName);
	   }
//
//  public static ProcessEngineConfiguration parseProcessEngineConfigurationFromResource(String resource, String beanName) {
//    Resource springResource = new ClassPathResource(resource);
//    return parseProcessEngineConfiguration(springResource, beanName);
//  }


	public static ProcessEngineConfiguration getProcessEngineConfiguration() {
		return processEngineConfiguration;
	}

}
