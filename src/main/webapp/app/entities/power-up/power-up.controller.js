(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('PowerUpController', PowerUpController);

    PowerUpController.$inject = ['$scope', '$state', 'PowerUp'];

    function PowerUpController ($scope, $state, PowerUp) {
        var vm = this;

        vm.powerUps = [];

        loadAll();

        function loadAll() {
            PowerUp.query(function(result) {
                vm.powerUps = result;
                vm.searchQuery = null;
            });
        }
    }
})();
