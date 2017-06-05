(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .controller('AnswerController', AnswerController);

    AnswerController.$inject = ['$scope', '$state', 'Answer'];

    function AnswerController ($scope, $state, Answer) {
        var vm = this;

        vm.answers = [];
        vm.answers2 = [];

        loadAll();

        function loadAll() {
            Answer.query(function(result) {
                vm.answers = result;
                vm.searchQuery = null;
            });
        }
        loadAllByAnswer();

        function loadAllByAnswer(){
            Answer.queryByAnswer(function(result){
                vm.answers2 = result;
                vm.searchQuery = null;
            })
        }
    }
})();
