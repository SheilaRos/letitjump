(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('UserCustomAtributesDialogController', UserCustomAtributesDialogController);

    UserCustomAtributesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserCustomAtributes', 'User'];

    function UserCustomAtributesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UserCustomAtributes, User) {
        var vm = this;

        vm.userCustomAtributes = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.userCustomAtributes.id !== null) {
                UserCustomAtributes.update(vm.userCustomAtributes, onSaveSuccess, onSaveError);
            } else {
                UserCustomAtributes.save(vm.userCustomAtributes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:userCustomAtributesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthday = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
