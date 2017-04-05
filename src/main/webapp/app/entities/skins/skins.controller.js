(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('SkinsController', SkinsController);

    SkinsController.$inject = ['$scope', '$state', 'Skins'];

    function SkinsController ($scope, $state, Skins) {
        var vm = this;

        vm.skins = [];

        loadAll();

        function loadAll() {
            Skins.query(function(result) {
                vm.skins = result;
                vm.searchQuery = null;
            });
        }
    }
})();
