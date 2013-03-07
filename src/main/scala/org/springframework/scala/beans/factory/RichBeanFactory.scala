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

import org.springframework.beans.factory.BeanFactory

/**
 * @author Arjen Poutsma
 */
class RichBeanFactory(val beanFactory: BeanFactory) {

	def getBean[T]()(implicit manifest: Manifest[T]): T = {
		beanFactory.getBean(manifestToClass(manifest))
	}

	def getBean[T](name: String)(implicit manifest: Manifest[T]): T = {
		beanFactory.getBean(name, manifestToClass(manifest))
	}

	private def manifestToClass[T](manifest: Manifest[T]): Class[T] = {
		manifest.runtimeClass.asInstanceOf[Class[T]]
	}

}