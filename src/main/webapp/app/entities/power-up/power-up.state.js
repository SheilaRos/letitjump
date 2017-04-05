(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('power-up', {
            parent: 'entity',
            url: '/power-up',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.powerUp.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/power-up/power-ups.html',
                    controller: 'PowerUpController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('powerUp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('power-up-detail', {
            parent: 'entity',
            url: '/power-up/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.powerUp.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/power-up/power-up-detail.html',
                    controller: 'PowerUpDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('powerUp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PowerUp', function($stateParams, PowerUp) {
                    return PowerUp.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'power-up',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('power-up-detail.edit', {
            parent: 'power-up-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/power-up/power-up-dialog.html',
                    controller: 'PowerUpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PowerUp', function(PowerUp) {
                            return PowerUp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('power-up.new', {
            parent: 'power-up',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/power-up/power-up-dialog.html',
                    controller: 'PowerUpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                priceGame: null,
                                pricePremium: null,
                                splashArt: null,
                                time: null,
                                attr: null,
                                attrValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('power-up', null, { reload: 'power-up' });
                }, function() {
                    $state.go('power-up');
                });
            }]
        })
        .state('power-up.edit', {
            parent: 'power-up',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/power-up/power-up-dialog.html',
                    controller: 'PowerUpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PowerUp', function(PowerUp) {
                            return PowerUp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('power-up', null, { reload: 'power-up' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('power-up.delete', {
            parent: 'power-up',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/power-up/power-up-delete-dialog.html',
                    controller: 'PowerUpDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PowerUp', function(PowerUp) {
                            return PowerUp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('power-up', null, { reload: 'power-up' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
