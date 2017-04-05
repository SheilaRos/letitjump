(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('PowerUp', PowerUp);

    PowerUp.$inject = ['$resource'];

    function PowerUp ($resource) {
        var resourceUrl =  'api/power-ups/:id';

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
