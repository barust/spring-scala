/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package org.springframework.scala.web.context.support

import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.support.{DefaultListableBeanFactory, DefaultBeanNameGenerator, BeanNameGenerator}
import org.springframework.scala.context.RichApplicationContext
import org.springframework.scala.context.function.FunctionalConfiguration
import org.springframework.scala.util.TypeTagUtils._
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext
import scala.collection.JavaConversions._
import scala.reflect.ClassTag

/**
 * @author Arjen Poutsma
 */
class FunctionalConfigWebApplicationContext
		extends AbstractRefreshableWebApplicationContext with RichApplicationContext {

	var beanNameGenerator: BeanNameGenerator = new DefaultBeanNameGenerator

	def loadBeanDefinitions(beanFactory: DefaultListableBeanFactory) {

		val configLocations = getConfigLocations
		if (configLocations != null) {
			for (configLocation <- configLocations) {
				try {
					val clazz: Class[_ <: FunctionalConfiguration] = getClassLoader.loadClass(configLocation).asInstanceOf[Class[_ <: FunctionalConfiguration]]
					if (logger.isInfoEnabled) {
						logger.info("Successfully resolved class for [" + configLocation + "]")
					}
					val configuration = BeanUtils.instantiate(clazz)
					configuration.register(beanFactory, getEnvironment, beanNameGenerator)
				}
				catch {
					case ex: ClassNotFoundException => {
						if (logger.isDebugEnabled) {
							logger.debug("Could not load class for config location [" + configLocation +
									             "]. " + ex)
						}
					}
				}
			}

		}


	}

	def apply[T: ClassTag]() = {
		getBean(typeToClass[T])
	}

	def apply[T: ClassTag](name: String) =
		getBean(name, typeToClass[T])

	def beanNamesForType[T: ClassTag](includeNonSingletons: Boolean, allowEagerInit: Boolean) =
		getBeanNamesForType(typeToClass[T], includeNonSingletons, allowEagerInit)

	def beansOfType[T: ClassTag](includeNonSingletons: Boolean, allowEagerInit: Boolean) =
		getBeansOfType(typeToClass[T], includeNonSingletons, allowEagerInit).toMap

}
