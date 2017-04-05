(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('MapDialogController', MapDialogController);

    MapDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Map', 'Level'];

    function MapDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Map, Level) {
        var vm = this;

        vm.map = entity;
        vm.clear = clear;
        vm.save = save;
        vm.levels = Level.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.map.id !== null) {
                Map.update(vm.map, onSaveSuccess, onSaveError);
            } else {
                Map.save(vm.map, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:mapUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
