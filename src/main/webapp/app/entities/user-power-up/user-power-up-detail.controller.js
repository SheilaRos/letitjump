(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('UserPowerUpDetailController', UserPowerUpDetailController);

    UserPowerUpDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserPowerUp', 'User', 'PowerUp'];

    function UserPowerUpDetailController($scope, $rootScope, $stateParams, previousState, entity, UserPowerUp, User, PowerUp) {
        var vm = this;

        vm.userPowerUp = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:userPowerUpUpdate', function(event, result) {
            vm.userPowerUp = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
