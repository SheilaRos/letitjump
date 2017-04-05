(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('SkinsDetailController', SkinsDetailController);

    SkinsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Skins', 'User'];

    function SkinsDetailController($scope, $rootScope, $stateParams, previousState, entity, Skins, User) {
        var vm = this;

        vm.skins = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:skinsUpdate', function(event, result) {
            vm.skins = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
