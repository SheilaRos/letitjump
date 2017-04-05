(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('PowerUpDetailController', PowerUpDetailController);

    PowerUpDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PowerUp'];

    function PowerUpDetailController($scope, $rootScope, $stateParams, previousState, entity, PowerUp) {
        var vm = this;

        vm.powerUp = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:powerUpUpdate', function(event, result) {
            vm.powerUp = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
