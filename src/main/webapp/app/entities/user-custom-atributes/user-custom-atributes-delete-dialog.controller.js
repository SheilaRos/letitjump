(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('UserCustomAtributesDeleteController',UserCustomAtributesDeleteController);

    UserCustomAtributesDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserCustomAtributes'];

    function UserCustomAtributesDeleteController($uibModalInstance, entity, UserCustomAtributes) {
        var vm = this;

        vm.userCustomAtributes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserCustomAtributes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
