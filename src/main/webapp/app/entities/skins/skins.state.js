(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('skins', {
            parent: 'entity',
            url: '/skins',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.skins.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skins/skins.html',
                    controller: 'SkinsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('skins');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('skins-detail', {
            parent: 'entity',
            url: '/skins/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.skins.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/skins/skins-detail.html',
                    controller: 'SkinsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('skins');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Skins', function($stateParams, Skins) {
                    return Skins.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'skins',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('skins-detail.edit', {
            parent: 'skins-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skins/skins-dialog.html',
                    controller: 'SkinsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Skins', function(Skins) {
                            return Skins.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('skins.new', {
            parent: 'skins',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skins/skins-dialog.html',
                    controller: 'SkinsDialogController',
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
                                attr: null,
                                attrValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('skins', null, { reload: 'skins' });
                }, function() {
                    $state.go('skins');
                });
            }]
        })
        .state('skins.edit', {
            parent: 'skins',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skins/skins-dialog.html',
                    controller: 'SkinsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Skins', function(Skins) {
                            return Skins.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('skins', null, { reload: 'skins' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('skins.delete', {
            parent: 'skins',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/skins/skins-delete-dialog.html',
                    controller: 'SkinsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Skins', function(Skins) {
                            return Skins.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('skins', null, { reload: 'skins' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
