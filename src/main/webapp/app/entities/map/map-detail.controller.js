(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('MapDetailController', MapDetailController);

    MapDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Map', 'Level'];

    function MapDetailController($scope, $rootScope, $stateParams, previousState, entity, Map, Level) {
        var vm = this;

        vm.map = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:mapUpdate', function(event, result) {
            vm.map = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
