(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('ForumEntry', ForumEntry);

    ForumEntry.$inject = ['$resource'];

    function ForumEntry ($resource) {
        var resourceUrl =  'api/forum-entries/:id';

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
