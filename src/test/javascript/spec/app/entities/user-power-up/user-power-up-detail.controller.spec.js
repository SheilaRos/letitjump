'use strict';

describe('Controller Tests', function() {

    describe('UserPowerUp Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserPowerUp, MockUser, MockPowerUp;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserPowerUp = jasmine.createSpy('MockUserPowerUp');
            MockUser = jasmine.createSpy('MockUser');
            MockPowerUp = jasmine.createSpy('MockPowerUp');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserPowerUp': MockUserPowerUp,
                'User': MockUser,
                'PowerUp': MockPowerUp
            };
            createController = function() {
                $injector.get('$controller')("UserPowerUpDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'letItJumpApp:userPowerUpUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
