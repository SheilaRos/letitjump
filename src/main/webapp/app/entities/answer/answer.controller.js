(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('AnswerController', AnswerController);

    AnswerController.$inject = ['$scope', '$state', 'Answer'];

    function AnswerController ($scope, $state, Answer) {
        var vm = this;

        vm.answers = [];

        loadAll();

        function loadAll() {
            Answer.query(function(result) {
                vm.answers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
