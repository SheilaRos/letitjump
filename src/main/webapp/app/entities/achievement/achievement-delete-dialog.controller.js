(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('AchievementDeleteController',AchievementDeleteController);

    AchievementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Achievement'];

    function AchievementDeleteController($uibModalInstance, entity, Achievement) {
        var vm = this;

        vm.achievement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Achievement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
