(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('UserCustomAtributes', UserCustomAtributes);

    UserCustomAtributes.$inject = ['$resource', 'DateUtils'];

    function UserCustomAtributes ($resource, DateUtils) {
        var resourceUrl =  'api/user-custom-atributes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'queryByRanking': { method: 'GET', isArray: true, url: 'api/user-custom-atributes/byRanking'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthday = DateUtils.convertLocalDateFromServer(data.birthday);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.birthday = DateUtils.convertLocalDateToServer(copy.birthday);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.birthday = DateUtils.convertLocalDateToServer(copy.birthday);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
