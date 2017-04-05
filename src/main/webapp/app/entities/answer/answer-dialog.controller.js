(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('AnswerDialogController', AnswerDialogController);

    AnswerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Answer', 'ForumEntry'];

    function AnswerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Answer, ForumEntry) {
        var vm = this;

        vm.answer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.forumentries = ForumEntry.query();
        vm.answers = Answer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.answer.id !== null) {
                Answer.update(vm.answer, onSaveSuccess, onSaveError);
            } else {
                Answer.save(vm.answer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:answerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
