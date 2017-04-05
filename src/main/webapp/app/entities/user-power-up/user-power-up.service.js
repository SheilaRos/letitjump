(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('UserPowerUp', UserPowerUp);

    UserPowerUp.$inject = ['$resource'];

    function UserPowerUp ($resource) {
        var resourceUrl =  'api/user-power-ups/:id';

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
