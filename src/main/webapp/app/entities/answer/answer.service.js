(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('Answer', Answer);

    Answer.$inject = ['$resource'];

    function Answer ($resource) {
        var resourceUrl =  'api/answers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'queryByAnswer': {method: 'GET', isArray: true, url: 'api/answers/forumEntry/{id}'},
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
