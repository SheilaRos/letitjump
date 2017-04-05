(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('ForumEntryDialogController', ForumEntryDialogController);

    ForumEntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ForumEntry', 'User', 'Answer'];

    function ForumEntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ForumEntry, User, Answer) {
        var vm = this;

        vm.forumEntry = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.answers = Answer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.forumEntry.id !== null) {
                ForumEntry.update(vm.forumEntry, onSaveSuccess, onSaveError);
            } else {
                ForumEntry.save(vm.forumEntry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:forumEntryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
