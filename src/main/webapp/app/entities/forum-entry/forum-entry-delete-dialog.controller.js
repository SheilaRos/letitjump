(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('ForumEntryDeleteController',ForumEntryDeleteController);

    ForumEntryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ForumEntry'];

    function ForumEntryDeleteController($uibModalInstance, entity, ForumEntry) {
        var vm = this;

        vm.forumEntry = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ForumEntry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
