'use strict';

describe('Controller Tests', function() {

    describe('ForumEntry Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockForumEntry, MockUser, MockAnswer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockForumEntry = jasmine.createSpy('MockForumEntry');
            MockUser = jasmine.createSpy('MockUser');
            MockAnswer = jasmine.createSpy('MockAnswer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ForumEntry': MockForumEntry,
                'User': MockUser,
                'Answer': MockAnswer
            };
            createController = function() {
                $injector.get('$controller')("ForumEntryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'letItJumpApp:forumEntryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
