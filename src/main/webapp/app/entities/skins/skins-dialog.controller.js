(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('SkinsDialogController', SkinsDialogController);

    SkinsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Skins', 'User'];

    function SkinsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Skins, User) {
        var vm = this;

        vm.skins = entity;
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
            if (vm.skins.id !== null) {
                Skins.update(vm.skins, onSaveSuccess, onSaveError);
            } else {
                Skins.save(vm.skins, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:skinsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
