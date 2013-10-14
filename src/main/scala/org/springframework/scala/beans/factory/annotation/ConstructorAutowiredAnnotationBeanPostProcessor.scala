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

package org.springframework.scala.beans.factory.annotation

import java.lang.reflect.Constructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import scala.beans.BeanProperty

/**
 * Simple extension of [[org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor]]
 * that detects the presence of @Autowired on a class with a single constructor.
 *
 * If both conditions are met then the constructor will be marked as a candidate
 * constructor, equivalent to marking the constructor with @Autowired in Java.
 *
 * This allows Scala inline constructors to be eligible for autowiring using the
 * following natural syntax.
 *
 * {{{
 * @Autowired
 * class MyBean(dep: MyDependency) { ... }
 * }}}
 *
 * @author Stephen Samuel
 * @author Arjen Poutsma
 */
@Component
class ConstructorAutowiredAnnotationBeanPostProcessor
		extends InstantiationAwareBeanPostProcessorAdapter with Ordered {

	@BeanProperty
	var order: Int = org.springframework.core.Ordered.LOWEST_PRECEDENCE


  override def determineCandidateConstructors(beanClass: Class[_], beanName: String): Array[Constructor[_]] = {
	  val constructors: Array[Constructor[_]] = beanClass.getDeclaredConstructors
	  if (isAutowiredClass(beanClass) && constructors.size == 1) {
		  constructors
    } else {
      null
    }
  }

  def isAutowiredClass(beanClass: Class[_]) =
	  AnnotationUtils.getAnnotation(beanClass, classOf[Autowired]) != null

}