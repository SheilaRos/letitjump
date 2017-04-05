(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('ForumEntryController', ForumEntryController);

    ForumEntryController.$inject = ['$scope', '$state', 'ForumEntry'];

    function ForumEntryController ($scope, $state, ForumEntry) {
        var vm = this;

        vm.forumEntries = [];

        loadAll();

        function loadAll() {
            ForumEntry.query(function(result) {
                vm.forumEntries = result;
                vm.searchQuery = null;
            });
        }
    }
})();
