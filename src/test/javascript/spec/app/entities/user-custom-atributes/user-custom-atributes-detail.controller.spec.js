'use strict';

describe('Controller Tests', function() {

    describe('UserCustomAtributes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserCustomAtributes, MockUser, MockLevel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserCustomAtributes = jasmine.createSpy('MockUserCustomAtributes');
            MockUser = jasmine.createSpy('MockUser');
            MockLevel = jasmine.createSpy('MockLevel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserCustomAtributes': MockUserCustomAtributes,
                'User': MockUser,
                'Level': MockLevel
            };
            createController = function() {
                $injector.get('$controller')("UserCustomAtributesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'letItJumpApp:userCustomAtributesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
