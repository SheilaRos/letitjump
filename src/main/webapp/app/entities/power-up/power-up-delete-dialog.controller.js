(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('PowerUpDeleteController',PowerUpDeleteController);

    PowerUpDeleteController.$inject = ['$uibModalInstance', 'entity', 'PowerUp'];

    function PowerUpDeleteController($uibModalInstance, entity, PowerUp) {
        var vm = this;

        vm.powerUp = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PowerUp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
