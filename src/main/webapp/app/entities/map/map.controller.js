(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('MapController', MapController);

    MapController.$inject = ['$scope', '$state', 'Map'];

    function MapController ($scope, $state, Map) {
        var vm = this;

        vm.maps = [];

        loadAll();

        function loadAll() {
            Map.query(function(result) {
                vm.maps = result;
                vm.searchQuery = null;
            });
        }
    }
})();
