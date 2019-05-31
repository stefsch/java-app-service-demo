/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

'use strict';
angular.module('todoApp')
    .factory('todoListSvc', ['$http', function ($http) {
        return {
            // Methods for Cosmos DB
            getItems: function () {
                return $http.get('api/todolist');
            },
            getItem: function (id) {
                return $http.get('api/todolist/' + id);
            },
            postItem: function (item) {
                return $http.post('api/todolist/', item);
            },
            putItem: function (item) {
                return $http.put('api/todolist/', item);
            },
            deleteItem: function (id) {
                return $http({
                    method: 'DELETE',
                    url: 'api/todolist/' + id
                });
            },

            // Methods for testing memory and CPU
            memoryTest: function () {
                console.log('Sent from todoListSvc.js');
                return $http.get('api/memoryTest');
            },
            cpuTest: function (cores, threads, load, duration) {
                console.log('Sent from todoListSrv.js');
                return $http.get('/api/cpuTest/cores/'+cores+'/threads/'+threads+'/load/'+load+'/duration/'+duration);
            }
        };
    }]);
