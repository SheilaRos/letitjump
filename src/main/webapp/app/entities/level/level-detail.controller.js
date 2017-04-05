(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('LevelDetailController', LevelDetailController);

    LevelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Level', 'Map', 'UserCustomAtributes'];

    function LevelDetailController($scope, $rootScope, $stateParams, previousState, entity, Level, Map, UserCustomAtributes) {
        var vm = this;

        vm.level = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:levelUpdate', function(event, result) {
            vm.level = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
