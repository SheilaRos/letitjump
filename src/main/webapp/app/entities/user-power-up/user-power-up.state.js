(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-power-up', {
            parent: 'entity',
            url: '/user-power-up?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.userPowerUp.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-power-up/user-power-ups.html',
                    controller: 'UserPowerUpController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userPowerUp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-power-up-detail', {
            parent: 'entity',
            url: '/user-power-up/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.userPowerUp.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-power-up/user-power-up-detail.html',
                    controller: 'UserPowerUpDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userPowerUp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserPowerUp', function($stateParams, UserPowerUp) {
                    return UserPowerUp.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-power-up',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-power-up-detail.edit', {
            parent: 'user-power-up-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-power-up/user-power-up-dialog.html',
                    controller: 'UserPowerUpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserPowerUp', function(UserPowerUp) {
                            return UserPowerUp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-power-up.new', {
            parent: 'user-power-up',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-power-up/user-power-up-dialog.html',
                    controller: 'UserPowerUpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                quantity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-power-up', null, { reload: 'user-power-up' });
                }, function() {
                    $state.go('user-power-up');
                });
            }]
        })
        .state('user-power-up.edit', {
            parent: 'user-power-up',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-power-up/user-power-up-dialog.html',
                    controller: 'UserPowerUpDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserPowerUp', function(UserPowerUp) {
                            return UserPowerUp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-power-up', null, { reload: 'user-power-up' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-power-up.delete', {
            parent: 'user-power-up',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-power-up/user-power-up-delete-dialog.html',
                    controller: 'UserPowerUpDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserPowerUp', function(UserPowerUp) {
                            return UserPowerUp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-power-up', null, { reload: 'user-power-up' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
