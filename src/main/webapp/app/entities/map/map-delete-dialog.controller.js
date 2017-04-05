(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('MapDeleteController',MapDeleteController);

    MapDeleteController.$inject = ['$uibModalInstance', 'entity', 'Map'];

    function MapDeleteController($uibModalInstance, entity, Map) {
        var vm = this;

        vm.map = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Map.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
