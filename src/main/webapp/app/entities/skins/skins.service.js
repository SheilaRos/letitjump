(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('Skins', Skins);

    Skins.$inject = ['$resource'];

    function Skins ($resource) {
        var resourceUrl =  'api/skins/:id';

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
