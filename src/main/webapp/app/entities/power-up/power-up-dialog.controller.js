(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('PowerUpDialogController', PowerUpDialogController);

    PowerUpDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PowerUp'];

    function PowerUpDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PowerUp) {
        var vm = this;

        vm.powerUp = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.powerUp.id !== null) {
                PowerUp.update(vm.powerUp, onSaveSuccess, onSaveError);
            } else {
                PowerUp.save(vm.powerUp, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:powerUpUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
