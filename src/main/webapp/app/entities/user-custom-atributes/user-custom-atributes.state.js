(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-custom-atributes', {
            parent: 'entity',
            url: '/ranking',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.userCustomAtributes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-custom-atributes/user-custom-atributes.html',
                    controller: 'UserCustomAtributesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userCustomAtributes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-custom-atributes-detail', {
            parent: 'entity',
            url: '/user-custom-atributes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.userCustomAtributes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-custom-atributes/user-custom-atributes-detail.html',
                    controller: 'UserCustomAtributesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userCustomAtributes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserCustomAtributes', function($stateParams, UserCustomAtributes) {
                    return UserCustomAtributes.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-custom-atributes',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-custom-atributes-detail.edit', {
            parent: 'user-custom-atributes-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-custom-atributes/user-custom-atributes-dialog.html',
                    controller: 'UserCustomAtributesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserCustomAtributes', function(UserCustomAtributes) {
                            return UserCustomAtributes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-custom-atributes.new', {
            parent: 'user-custom-atributes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-custom-atributes/user-custom-atributes-dialog.html',
                    controller: 'UserCustomAtributesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                birthday: null,
                                moneyGame: null,
                                moneyPremium: null,
                                score: null,
                                sex: null,
                                level: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-custom-atributes', null, { reload: 'user-custom-atributes' });
                }, function() {
                    $state.go('user-custom-atributes');
                });
            }]
        })
        .state('user-custom-atributes.edit', {
            parent: 'user-custom-atributes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-custom-atributes/user-custom-atributes-dialog.html',
                    controller: 'UserCustomAtributesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserCustomAtributes', function(UserCustomAtributes) {
                            return UserCustomAtributes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-custom-atributes', null, { reload: 'user-custom-atributes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-custom-atributes.delete', {
            parent: 'user-custom-atributes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-custom-atributes/user-custom-atributes-delete-dialog.html',
                    controller: 'UserCustomAtributesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserCustomAtributes', function(UserCustomAtributes) {
                            return UserCustomAtributes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-custom-atributes', null, { reload: 'user-custom-atributes' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
