(function() {
    'use strict';
    angular
        .module('letItJumpApp')
        .factory('UserCustomAtributes', UserCustomAtributes);

    UserCustomAtributes.$inject = ['$resource', 'DateUtils'];

    function UserCustomAtributes ($resource, DateUtils) {
        //Esta es la dirección a la que irán los datos
        var resourceUrl =  'api/user-custom-atributes/:id';
        //Desde el archivo UserCustomAtributes hemos creado una query que nos devuelve todos los jugadores de la app según su score
        //Dicha función se llama getAllUserCustomAtributesByRanking()
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
