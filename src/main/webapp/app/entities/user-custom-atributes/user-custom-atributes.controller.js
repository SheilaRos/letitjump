(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('UserCustomAtributesController', UserCustomAtributesController);

    UserCustomAtributesController.$inject = ['$scope', '$state', 'UserCustomAtributes'];

    function UserCustomAtributesController ($scope, $state, UserCustomAtributes) {
        var vm = this;
        //Aquí creas el array que contendrá los datos
        vm.userCustomAtributes = [];
        vm.userCustomAtributesByRanking = [];

        loadAll();

        function loadAll() {
            UserCustomAtributes.query(function(result) {
                vm.userCustomAtributes = result;
                vm.searchQuery = null;
            });
        }
        //Aquí creas la función que llenará de datos el array previamente creado
        loadAllByRanking();

        function loadAllByRanking() {
            UserCustomAtributes.queryByRanking(function(result) {
                vm.userCustomAtributesByRanking = result;
                vm.searchQuery = null;
            });
        }
    }
})();
