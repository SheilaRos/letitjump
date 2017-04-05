(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('SkinsDeleteController',SkinsDeleteController);

    SkinsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Skins'];

    function SkinsDeleteController($uibModalInstance, entity, Skins) {
        var vm = this;

        vm.skins = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Skins.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
