(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('UserPowerUpDialogController', UserPowerUpDialogController);

    UserPowerUpDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserPowerUp', 'User', 'PowerUp'];

    function UserPowerUpDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserPowerUp, User, PowerUp) {
        var vm = this;

        vm.userPowerUp = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.powerups = PowerUp.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userPowerUp.id !== null) {
                UserPowerUp.update(vm.userPowerUp, onSaveSuccess, onSaveError);
            } else {
                UserPowerUp.save(vm.userPowerUp, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:userPowerUpUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
