(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('ForumEntryDetailController', ForumEntryDetailController);

    ForumEntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ForumEntry', 'User', 'Answer'];

    function ForumEntryDetailController($scope, $rootScope, $stateParams, previousState, entity, ForumEntry, User, Answer) {
        var vm = this;

        vm.forumEntry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('letItJumpApp:forumEntryUpdate', function(event, result) {
            vm.forumEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
