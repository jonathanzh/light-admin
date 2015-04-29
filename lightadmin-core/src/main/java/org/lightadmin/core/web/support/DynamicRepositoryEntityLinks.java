/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.web.support;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;

import java.io.Serializable;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.data.rest.webmvc.support.PagingAndSortingTemplateVariables;
import org.springframework.plugin.core.PluginRegistry;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class DynamicRepositoryEntityLinks extends RepositoryEntityLinks {

    public DynamicRepositoryEntityLinks(Repositories repositories, ResourceMappings mappings, RepositoryRestConfiguration config, PagingAndSortingTemplateVariables templateVariables, PluginRegistry<BackendIdConverter, Class<?>> idConverters) {
        super(repositories, mappings, config, templateVariables, idConverters);
    }

    public Link linkForFilePropertyLink(Object instance, PersistentProperty persistentProperty) {
        PersistentEntity persistentEntity = persistentProperty.getOwner();
        Serializable id = idValue(instance, persistentEntity);

        return super.linkForSingleResource(persistentEntity.getType(), id).slash(persistentProperty.getName()).slash("file").withSelfRel();
    }

    @Override
    public Link linkToSingleResource(Class<?> type, Object id) {
        if (id == null) {
            return linkFor(type).slash("new").withSelfRel();
        }
        return super.linkToSingleResource(type, id);
    }

    private Serializable idValue(Object instance, PersistentEntity persistentEntity) {
        return (Serializable) new DirectFieldAccessFallbackBeanWrapper(instance).getPropertyValue(persistentEntity.getIdProperty().getName());
    }
}