(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('LevelDialogController', LevelDialogController);

    LevelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Level', 'Map', 'UserCustomAtributes'];

    function LevelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Level, Map, UserCustomAtributes) {
        var vm = this;

        vm.level = entity;
        vm.clear = clear;
        vm.save = save;
        vm.maps = Map.query({filter: 'level-is-null'});
        $q.all([vm.level.$promise, vm.maps.$promise]).then(function() {
            if (!vm.level.map || !vm.level.map.id) {
                return $q.reject();
            }
            return Map.get({id : vm.level.map.id}).$promise;
        }).then(function(map) {
            vm.maps.push(map);
        });
        vm.usercustomatributes = UserCustomAtributes.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.level.id !== null) {
                Level.update(vm.level, onSaveSuccess, onSaveError);
            } else {
                Level.save(vm.level, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('letItJumpApp:levelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
