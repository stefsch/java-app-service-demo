/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.dao;

import com.microsoft.azure.sample.model.TodoItem;
//import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepository extends CrudRepository<TodoItem, String> {

    // Data source wiring: https://www.concretepage.com/spring-5/spring-data-crudrepository-example

}