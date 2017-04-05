(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('AchievementController', AchievementController);

    AchievementController.$inject = ['$scope', '$state', 'Achievement'];

    function AchievementController ($scope, $state, Achievement) {
        var vm = this;

        vm.achievements = [];

        loadAll();

        function loadAll() {
            Achievement.query(function(result) {
                vm.achievements = result;
                vm.searchQuery = null;
            });
        }
    }
})();
