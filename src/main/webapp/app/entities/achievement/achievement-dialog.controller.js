(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('AchievementDialogController', AchievementDialogController);

    AchievementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Achievement', 'User'];

    function AchievementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Achievement, User) {
        var vm = this;

        vm.achievement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.achievement.id !== null) {
                Achievement.update(vm.achievement, onSaveSuccess, onSaveError);
            } else {
                Achievement.save(vm.achievement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:achievementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
