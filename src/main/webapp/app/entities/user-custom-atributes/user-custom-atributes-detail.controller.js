(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('UserCustomAtributesDetailController', UserCustomAtributesDetailController);

    UserCustomAtributesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserCustomAtributes', 'User', 'Level'];

    function UserCustomAtributesDetailController($scope, $rootScope, $stateParams, previousState, entity, UserCustomAtributes, User, Level) {
        var vm = this;

        vm.userCustomAtributes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:userCustomAtributesUpdate', function(event, result) {
            vm.userCustomAtributes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
