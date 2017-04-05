(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('UserPowerUpDeleteController',UserPowerUpDeleteController);

    UserPowerUpDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserPowerUp'];

    function UserPowerUpDeleteController($uibModalInstance, entity, UserPowerUp) {
        var vm = this;

        vm.userPowerUp = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserPowerUp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
