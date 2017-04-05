(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('Achievement', Achievement);

    Achievement.$inject = ['$resource'];

    function Achievement ($resource) {
        var resourceUrl =  'api/achievements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
