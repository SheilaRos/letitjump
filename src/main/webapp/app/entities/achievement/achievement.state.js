(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('achievement', {
            parent: 'entity',
            url: '/achievement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.achievement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/achievement/achievements.html',
                    controller: 'AchievementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('achievement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('achievement-detail', {
            parent: 'entity',
            url: '/achievement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.achievement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/achievement/achievement-detail.html',
                    controller: 'AchievementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('achievement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Achievement', function($stateParams, Achievement) {
                    return Achievement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'achievement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('achievement-detail.edit', {
            parent: 'achievement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/achievement/achievement-dialog.html',
                    controller: 'AchievementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Achievement', function(Achievement) {
                            return Achievement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('achievement.new', {
            parent: 'achievement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/achievement/achievement-dialog.html',
                    controller: 'AchievementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                prize: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('achievement', null, { reload: 'achievement' });
                }, function() {
                    $state.go('achievement');
                });
            }]
        })
        .state('achievement.edit', {
            parent: 'achievement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/achievement/achievement-dialog.html',
                    controller: 'AchievementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Achievement', function(Achievement) {
                            return Achievement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('achievement', null, { reload: 'achievement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('achievement.delete', {
            parent: 'achievement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/achievement/achievement-delete-dialog.html',
                    controller: 'AchievementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Achievement', function(Achievement) {
                            return Achievement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('achievement', null, { reload: 'achievement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
