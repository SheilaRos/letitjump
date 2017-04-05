(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('map', {
            parent: 'entity',
            url: '/map',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.map.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/map/maps.html',
                    controller: 'MapController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('map');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('map-detail', {
            parent: 'entity',
            url: '/map/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.map.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/map/map-detail.html',
                    controller: 'MapDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('map');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Map', function($stateParams, Map) {
                    return Map.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'map',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('map-detail.edit', {
            parent: 'map-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/map/map-dialog.html',
                    controller: 'MapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Map', function(Map) {
                            return Map.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('map.new', {
            parent: 'map',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/map/map-dialog.html',
                    controller: 'MapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('map', null, { reload: 'map' });
                }, function() {
                    $state.go('map');
                });
            }]
        })
        .state('map.edit', {
            parent: 'map',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/map/map-dialog.html',
                    controller: 'MapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Map', function(Map) {
                            return Map.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('map', null, { reload: 'map' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('map.delete', {
            parent: 'map',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/map/map-delete-dialog.html',
                    controller: 'MapDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Map', function(Map) {
                            return Map.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('map', null, { reload: 'map' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
