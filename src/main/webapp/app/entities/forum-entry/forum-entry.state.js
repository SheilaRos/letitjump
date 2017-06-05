(function() {
    'use strict';

    angular
        .module('letItJumpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('forum-entry', {
            parent: 'entity',
            url: '/forum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.forumEntry.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forum-entry/forum-entries.html',
                    controller: 'ForumEntryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('forumEntry');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('forum-entry-detail', {
            parent: 'entity',
            url: '/forum-entry/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'letItJumpApp.forumEntry.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forum-entry/forum-entry-detail.html',
                    controller: 'ForumEntryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('forumEntry');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ForumEntry', function($stateParams, ForumEntry) {
                    return ForumEntry.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'forum-entry',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('forum-entry-detail.edit', {
            parent: 'forum-entry-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum-entry/forum-entry-dialog.html',
                    controller: 'ForumEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ForumEntry', function(ForumEntry) {
                            return ForumEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forum-entry.new', {
            parent: 'forum-entry',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum-entry/forum-entry-dialog.html',
                    controller: 'ForumEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                text: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('forum-entry', null, { reload: 'forum-entry' });
                }, function() {
                    $state.go('forum-entry');
                });
            }]
        })
        .state('forum-entry.edit', {
            parent: 'forum-entry',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum-entry/forum-entry-dialog.html',
                    controller: 'ForumEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ForumEntry', function(ForumEntry) {
                            return ForumEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forum-entry', null, { reload: 'forum-entry' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forum-entry.delete', {
            parent: 'forum-entry',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forum-entry/forum-entry-delete-dialog.html',
                    controller: 'ForumEntryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ForumEntry', function(ForumEntry) {
                            return ForumEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forum-entry', null, { reload: 'forum-entry' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
