(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('AchievementDetailController', AchievementDetailController);

    AchievementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Achievement', 'User'];

    function AchievementDetailController($scope, $rootScope, $stateParams, previousState, entity, Achievement, User) {
        var vm = this;

        vm.achievement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:achievementUpdate', function(event, result) {
            vm.achievement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
