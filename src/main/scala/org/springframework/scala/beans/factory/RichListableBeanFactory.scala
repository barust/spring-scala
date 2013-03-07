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

package org.springframework.scala.beans.factory

import org.springframework.beans.factory.ListableBeanFactory
import scala.collection.JavaConversions._
import java.lang.annotation.Annotation

/**
 * @author Arjen Poutsma
 */
class RichListableBeanFactory(val beanFactory: ListableBeanFactory) {

	def getBeanNamesForType[T](includeNonSingletons: Boolean = true,
	                           allowEagerInit: Boolean = true)
	                          (implicit manifest: Manifest[T]): Seq[String] = {
		beanFactory
				.getBeanNamesForType(manifestToClass(manifest), includeNonSingletons, allowEagerInit)
	}

	def getBeansOfType[T](includeNonSingletons: Boolean = true,
	                      allowEagerInit: Boolean = true)
	                     (implicit manifest: Manifest[T]): Map[String, T] = {
		beanFactory
				.getBeansOfType(manifestToClass(manifest), includeNonSingletons, allowEagerInit)
				.toMap
	}

	def getBeansWithAnnotation[T <: Annotation]()
	                                           (implicit manifest: Manifest[T]): Map[String, Any] = {
		beanFactory.getBeansWithAnnotation(manifestToClass(manifest)).toMap
	}

	def findAnnotationOnBean[T <: Annotation](beanName: String)
	                                         (implicit manifest: Manifest[T]): T = {
		beanFactory.findAnnotationOnBean(beanName, manifestToClass(manifest))
	}

	private def manifestToClass[T](manifest: Manifest[T]): Class[T] = {
		manifest.runtimeClass.asInstanceOf[Class[T]]
	}


}
